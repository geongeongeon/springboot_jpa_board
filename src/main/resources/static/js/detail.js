$(document).ready(function() {
    $('#icon_answer').hover(function() {
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

    $('#btn_modifyAnswer').on('click', function() {
        $('#view_answerContent').addClass('d-none');
        $('#view_answerModifyDate').addClass('d-none');
        $('#edit_answerContent').removeClass('d-none');
        $('#edit_answerModifyBtn').addClass('d-none');
    });
});