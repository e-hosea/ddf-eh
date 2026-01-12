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

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.util.NamedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SolrClientWrapper extends SolrClient {
  private static final Logger LOGGER = LoggerFactory.getLogger(SolrClientWrapper.class);
  private final SolrClient solrClient;
  private final String username;
  private final String password;

  public SolrClientWrapper(SolrClient solrClient, String username, String password) {
    LOGGER.info("PATPAT: SolrClientWrapper.constructor()3");

    this.solrClient = solrClient; // Original SolrClient (e.g., CloudHttp2SolrClient)
    this.username = username;
    this.password = password;
  }

  @Override
  public NamedList<Object> request(SolrRequest<?> request, String collection)
      throws SolrServerException, IOException {
    LOGGER.info("PATPAT: SolrClientWrapper.request()3");

    // Attach credentials to the SolrRequest before processing
    request.setBasicAuthCredentials(username, password);
    return solrClient.request(
        request, collection); // Delegate the request to the original SolrClient
  }

  @Override
  public void close() throws IOException {
    LOGGER.info("PATPAT: SolrClientWrapper.close()3");

    solrClient.close();
  }

  // Optionally override other methods here if needed
}
