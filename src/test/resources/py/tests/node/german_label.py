def german_label(value):
    results = []
    p = py_tf.namedNode("http://example.org/ns#germanLabel")
    s = _data.find(value, p, None)
    while t := s.next():
        object = t.getObject()
        if (object.getTermType() != "Literal") | (not object.getLanguage().startswith("de")):
            results.append({"value": object})
    return results
