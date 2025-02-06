$(document).ready(function() {
    $('#icon_comment').hover(function() {
        $(this).removeClass('bi-chat');
        $(this).addClass('bi-chat-fill');
    }, function() {
        $(this).removeClass('bi-chat-fill');
        $(this).addClass('bi-chat');
    });

    $('#icon_like').hover(function() {
        $(this).removeClass('bi-hand-thumbs-up');
        $(this).addClass('bi-hand-thumbs-up-fill');
    }, function() {
        $(this).removeClass('bi-hand-thumbs-up-fill');
        $(this).addClass('bi-hand-thumbs-up');
    });

    $('#icon_dislike').hover(function() {
        $(this).removeClass('bi-hand-thumbs-down');
        $(this).addClass('bi-hand-thumbs-down-fill');
    }, function() {
        $(this).removeClass('bi-hand-thumbs-down-fill');
        $(this).addClass('bi-hand-thumbs-down');
    });

    $('textarea').on('input', function() {
        this.style.height = 'auto';
        this.style.height = (this.scrollHeight) + 'px';
    });
});