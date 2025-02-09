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

    $(document).on('click', '[id^="btn_modifyAnswer_"]', function() {
        var answer_id = $(this).attr('id').split('_')[2];

        $('#view_answerContent_' + answer_id).addClass('d-none');
        $('#view_answerModifyDate_' + answer_id).addClass('d-none');
        $('#form_editAnswer_' + answer_id).removeClass('d-none');
        $('#edit_textarea_' + answer_id).focus().get(0).setSelectionRange($('#edit_textarea_' + answer_id).val().length, $('#edit_textarea_' + answer_id).val().length);
    });
});