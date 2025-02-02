const {
	ClassicEditor,
	Autoformat,
  Alignment,
	AutoLink,
	Autosave,
	BlockQuote,
	Bold,
	Essentials,
	FontBackgroundColor,
	FontColor,
	FontSize,
	Italic,
	Link,
	List,
	ListProperties,
	Paragraph,
	Table,
	TableCaption,
	TableCellProperties,
	TableColumnResize,
	TableProperties,
	TableToolbar,
	TextTransformation,
	Underline
} = window.CKEDITOR;

const LICENSE_KEY =
	'eyJhbGciOiJFUzI1NiJ9.eyJleHAiOjE3Mzg2MjcxOTksImp0aSI6ImQyMzgwMjgzLWI4NTgtNGM5Mi05Njc4LWIzMTZhM2JmNzI5ZiIsImxpY2Vuc2VkSG9zdHMiOlsiKi53ZWJjb250YWluZXIuaW8iLCIqLmpzaGVsbC5uZXQiLCIqLmNzcC5hcHAiLCJjZHBuLmlvIiwiMTI3LjAuMC4xIiwibG9jYWxob3N0IiwiMTkyLjE2OC4qLioiLCIxMC4qLiouKiIsIjE3Mi4qLiouKiIsIioudGVzdCIsIioubG9jYWxob3N0IiwiKi5sb2NhbCJdLCJkaXN0cmlidXRpb25DaGFubmVsIjpbImNsb3VkIiwiZHJ1cGFsIiwic2giXSwibGljZW5zZVR5cGUiOiJldmFsdWF0aW9uIiwidmMiOiI0YWY1MWNlMSJ9.0R4iVzX2XqkRb9CkSDZJTtqOmdwtzeJXsQpbQIGGP_IESxHPPCdGhsZuAc3SaSX1jfCiT48RJ7KN9b5mJgNngw';

const editorConfig = {
	toolbar: {
		items: [
			'fontSize', 'bold', 'italic', 'underline',
			'|',
			'fontColor', 'fontBackgroundColor',
			'|',
			'alignment', 'bulletedList', 'numberedList',
			'|',
			'link', 'blockQuote', 'insertTable',
		],
		shouldNotGroupWhenFull: false
	},
	plugins: [
		Autoformat,
		Alignment,
		AutoLink,
		Autosave,
		BlockQuote,
		Bold,
		Essentials,
		FontBackgroundColor,
		FontColor,
		FontSize,
		Italic,
		Link,
		List,
		ListProperties,
		Paragraph,
		Table,
		TableCaption,
		TableCellProperties,
		TableColumnResize,
		TableProperties,
		TableToolbar,
		TextTransformation,
		Underline
	],
	fontSize: {
		options: [10, 12, 14, 'default', 18, 20, 22],
		supportAllValues: true
	},
	language: 'ko',
	licenseKey: LICENSE_KEY,
	link: {
		addTargetToExternalLinks: true,
		defaultProtocol: 'https://',
	},
	list: {
		properties: {
			styles: true,
			startIndex: true,
			reversed: true
		}
	},
	table: {
		contentToolbar: ['tableColumn', 'tableRow', 'mergeTableCells', 'tableProperties', 'tableCellProperties']
	}
};

ClassicEditor.create(document.querySelector('#editor'), editorConfig);