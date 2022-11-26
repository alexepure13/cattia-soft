function increaseValue(fieldId) {
    var value = parseInt(document.getElementById(fieldId).value, 10);
    value = isNaN(value) ? 0 : value;
    value++;
    document.getElementById(fieldId).value = value;
}

function decreaseValue(fieldId) {
    var value = parseInt(document.getElementById(fieldId).value, 10);
    value = isNaN(value) ? 0 : value;
    value < 1 ? value = 1 : '';
    if(value > 1){
        value--;
    }
    document.getElementById(fieldId).value = value;
}