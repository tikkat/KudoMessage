function showNewContactBox(button) {
    if ($('#newContactBox').is(':visible')) {
        hideNewContactBox();
    } else {
        var position = $(button).position();
        var left = position.left + 20;
        var top = position.top + 34;

        $("#newContactBox").css({top: top + 'px', left: left + 'px'});
        $("#newContactBox").slideDown("slow");
    }
}

function hideNewContactBox() {
    $("#newContactBox").hide();
}

function conversationOnHover(button) {
    $(button).parent().css('background', '#F3F3F3');
}

function conversationOnNotHover(button) {
    $(button).parent().css('background', 'transparent');
}

function showCross(div) {
    $(div).children().children().css('visibility', 'visible');
}

function hideCross(div) {
    $(div).children().children().css('visibility', 'hidden');
}

function setNewConversationTextBox(sender) {
    $(".newConversationTextBox").val($(sender).val());
}

function playSentSound() {
    $('.player_audio').trigger("play");
}