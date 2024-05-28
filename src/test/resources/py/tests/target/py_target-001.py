def py_target001(_this):
    target_nodes = []
    rdf_type = py_tf.namedNode("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")
    owl_thing = py_tf.namedNode("http://www.w3.org/2002/07/owl#Thing")
    it = _data.find(_this, rdf_type, owl_thing)
    while t := it.next():
        target_nodes.append(t.getSubject())
    return target_nodes
