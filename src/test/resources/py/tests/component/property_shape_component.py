def property_shape(this, path, min_cardinality):
    it = _data.find(this, path, None)
    count = 0
    while it.next():
        count += 1
    return count >= int(min_cardinality.getLex())
