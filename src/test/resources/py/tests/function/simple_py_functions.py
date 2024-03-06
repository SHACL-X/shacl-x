def boolean_function():
    return True


def float_function():
    return 4.2


def integer_function():
    return 42


def node_function():
    return py_tf.namedNode("http://aldi.de")


def string_function():
    return "Hello"


def with_arguments(_arg1, _arg2):
    return py_tf.namedNode("http://aldi.de/product_" + _arg1.getValue() + "_" + _arg2.getValue())
