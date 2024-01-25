def inverse_path_single():
    return rdf_query(_data).path("sh:Shape", {"inverse": T("rdfs:subClassOf")}, "?subClass").get_count()


def one_or_more():
    return rdf_query(_data).path(py_tf.namedNode("http://datashapes.org/js/tests-rdfquery/paths.test#MergedClass"),
                                 {"oneOrMore": T("rdfs:subClassOf")}, "?superClass").get_count()


def or_path_label_or_comment():
    return rdf_query(_data).path("sh:Shape", {"or": [T("rdfs:label"), T("rdfs:comment")]}, "?text").get_count()


def predicate_path_single():
    return rdf_query(_data).path("sh:Shape", "rdfs:label", "?label").get_node("?label")


def predicate_path_chain():
    return rdf_query(_data).path("sh:PropertyShape", "rdfs:subClassOf", "?Shape").path("?Shape", "rdfs:label",
                                                                                       "?label").get_node("?label")


def sequence_path_2():
    return rdf_query(_data).path("owl:Thing", [T("rdf:type"), T("rdfs:subClassOf")], "?Class").get_node("?Class")


def sequence_path_3():
    return rdf_query(_data).path("owl:Thing", [T("rdf:type"), T("rdfs:subClassOf"), T("rdf:type")], "?type").get_node(
        "?type")


def zero_or_one():
    return rdf_query(_data).path("owl:Thing", {"zeroOrOne": T("rdf:type")}, "?type").get_count()


def zero_or_more():
    return rdf_query(_data).path(py_tf.namedNode("http://datashapes.org/js/tests-rdfquery/paths.test#MergedClass"),
                                 {"zeroOrMore": T("rdfs:subClassOf")}, "?superClass").get_count()
