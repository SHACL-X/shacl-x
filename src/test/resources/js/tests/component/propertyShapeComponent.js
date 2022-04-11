function propertyShape($this, $path, $minCardinality) {
    let it = $data.find($this, $path, null);
    let count = 0;
    for (let t = it.next(); t; t = it.next()) {
        count++;
    }
    return count >= $minCardinality.lex;
}