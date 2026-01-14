/**
 * Copyright (c) Codice Foundation
 *
 * <p>This is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version.
 *
 * <p>This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details. A copy of the GNU Lesser General Public
 * License is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 */
package ddf.catalog.source.solr;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.util.NamedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolrClientAuthWrapper extends SolrClient {
  private static final Logger LOGGER = LoggerFactory.getLogger(SolrClientAuthWrapper.class);
  private static final String SOLR_USE_BASIC_AUTH = "solr.useBasicAuth";
  private static final String SOLR_USERNAME = "solr.username";
  private static final String SOLR_PASSWORD = "solr.password";

  private final SolrClient solrClient;

  public SolrClientAuthWrapper(SolrClient solrClient) {
    this.solrClient = solrClient;
  }

  @Override
  public NamedList<Object> request(SolrRequest<?> request, String collection)
      throws SolrServerException, IOException {
    boolean useBasicAuth =
        AccessController.doPrivileged(
            (PrivilegedAction<Boolean>)
                () -> Boolean.valueOf(System.getProperty(SOLR_USE_BASIC_AUTH)));
    LOGGER.info("PATPAT: solr.useBasicAuth: {}", useBasicAuth);

    if (useBasicAuth) {
      request.setBasicAuthCredentials(getSolrUsername(), getSolrPassword());
      LOGGER.info("PATPAT: solr.username: {}", getSolrUsername());
    }

    return solrClient.request(request, collection);
  }

  @Override
  public void close() throws IOException {
    solrClient.close();
  }

  private String getSolrUsername() {
    return AccessController.doPrivileged(
        (PrivilegedAction<String>) () -> System.getProperty(SOLR_USERNAME));
  }

  private String getSolrPassword() {
    return AccessController.doPrivileged(
        (PrivilegedAction<String>) () -> System.getProperty(SOLR_PASSWORD));
  }
}
