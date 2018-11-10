function runCommand() {
    var $ = window["jQuery"];
    $.getJSON("/run?commandStr=" + $("#command").val(), function (result) {
        if (result) {
            $("#commandResult").html(result.message);
        }
    });
}