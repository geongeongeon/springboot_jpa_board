$(document).ready(function() {
    $('#summernote').summernote({
        lang: 'ko-KR',
        placeholder: '내용을 입력하세요.',
        height: 400,
        toolbar: [
            ['fontsize', ['fontsize']],
            ['font', ['bold', 'italic', 'underline', 'strikethrough', 'color']],
            ['para', ['ul', 'ol', 'paragraph']],
            ['insert', ['table', 'link', 'hr']],
            ['view', ['codeview']]
        ],
        fontSizes: ['9', '10', '11', '12', '14', '16', '24', '36'],
        disableDragAndDrop: true,
        callbacks: {
            onKeydown: function(e) {
                if (e.keyCode === 9) {
                    e.preventDefault();
                    document.execCommand('insertText', false, '    ');
                }
            },
            onInit: function () {
                $('.note-editable').css('font-family', 'GmarketSansMedium');
                $('.note-editable').css('line-height', '1.3');
                $('#summernote').show();
            }
        }
    });
});