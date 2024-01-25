def predicate_path_single():
    return rdf_query(_data).path("sh:Shape", "rdfs:label", "?label").get_node("?label")
