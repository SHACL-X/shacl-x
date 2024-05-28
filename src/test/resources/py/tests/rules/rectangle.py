ns = "http://datashapes.org/py/tests/rules/rectangle.test#"


def compute_area(_this):
    width = get_property(_this, "width")
    height = get_property(_this, "height")
    area = py_tf.literal(int(width.getLex()) * int(height.getLex()), width.getDatatype())
    area_property = py_tf.namedNode(ns + "area")
    return [ [_this, area_property, area] ]


def get_property(_this, name):
    it = _data.find(_this, py_tf.namedNode(ns + name), None)
    result = it.next().getObject()
    it.close()
    return result
