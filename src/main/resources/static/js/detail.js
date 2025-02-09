$(document).ready(function() {
    if (window.location.hash.trim() !== "") {
        $('#commentsSection').addClass('show');
    }

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

        var original_content = $('#edit_textarea_' + answer_id).val();
        $('#edit_textarea_' + answer_id).focus().get(0).setSelectionRange($('#edit_textarea_' + answer_id).val().length, $('#edit_textarea_' + answer_id).val().length);

        $(document).on('click', '#btn_cancelEdit_' + answer_id, function() {
            $('#view_answerContent_' + answer_id).removeClass('d-none');
            $('#view_answerModifyDate_' + answer_id).removeClass('d-none');
            $('#form_editAnswer_' + answer_id).addClass('d-none');
            $('#edit_textarea_' + answer_id).val(original_content);
        })
    });

    $(document).on('click', '#btn_like', function() {
        var postId = $(this).data('post-id');
        var loginUser = $(this).data('login-user');
        var csrfToken = $('meta[name="_csrf"]').attr('content');
        var csrfHeader = $('meta[name="_csrf_header"]').attr('content');
        handleLikeDislike('like', postId, loginUser, csrfToken, csrfHeader);
    });

    $(document).on('click', '#btn_dislike', function() {
        var postId = $(this).data('post-id');
        var loginUser = $(this).data('login-user');
        var csrfToken = $('meta[name="_csrf"]').attr('content');
        var csrfHeader = $('meta[name="_csrf_header"]').attr('content');
        handleLikeDislike('dislike', postId, loginUser, csrfToken, csrfHeader);
    });
});

function handleLikeDislike(action, postId, loginUser, csrfToken, csrfHeader) {
    $.ajax({
        url: "/post/" + action,
        type: "POST",
        contentType: "application/json",
        dataType: "json",
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeader, csrfToken);
        },
        data: JSON.stringify({
            postId: postId,
            loginUser: loginUser
        }),
        success: function(response) {
            console.log('응답:', response);
            if (response.success) {
                $('#postModalLabel').text("성공");

                if (action === "like") {
                    $('#postLikeCount').text(response.likeCount);
                    $('#postModalText').text("게시글에 '좋아요'를 눌렀습니다.");
                } else if (action === "dislike") {
                    $('#postDislikeCount').text(response.dislikeCount);
                    $('#postModalText').text("게시글에 '싫어요'를 눌렀습니다.");
                }

                $('#postModal').modal('show');
            } else {
                $('#postModalLabel').text("실패");

                if (action === "like") {
                    $('#postModalText').text("이미 '좋아요'를 누른 게시글입니다.");
                } else if (action === "dislike") {
                    $('#postModalText').text("이미 '싫어요'를 누른 게시글입니다.");
                }

                $('#postModal').modal('show');
            }
        },
        error: function(xhr, status, error) {
            console.log('에러:', error);
        }
    });
}