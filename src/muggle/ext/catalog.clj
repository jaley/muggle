(ns muggle.ext.catalog
  "Protocol definition for extracting data file listings.")

(defprotocol Catalog
  (listing [catalog uri]
    "Return a sequence of data files (URIs) under data set at given URI"))
