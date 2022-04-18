function clearFlash() {
    $("#flashContainer").empty();
}

function addFlash(cssClass, message) {
    $("#flashContainer").append("<div class='flash " + cssClass + "'>" + message + "</div>");
}
