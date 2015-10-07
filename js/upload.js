
Dropzone.autoDiscover = true;

var dropzone;
Dropzone.options.myDropzone = {
    uploadMultiple: false,
    parallelUploads: 100,
    maxFilesize: 8,
    paramName: "file",
    addRemoveLinks: true,
    dictRemoveFile: 'Remove',
    dictFileTooBig: 'Image is bigger than 8MB',
    autoProcessQueue: false,
    init: function () {
        window.alert("dropzone");
        dropzone = this;
    }
};

$(function() {
    $("#uploadBtn").click(function () {

        document.getElementById("response").innerHTML = "Uploading . . .";

        var i = 0, len = dropzone.files.length;
        window.alert(len);
        for (; i < len; i++) {
            console.error("before");
            file = dropzone.files[i];
            console.error("after");
            $.ajax({
                url: window.location.href,
                type: "POST",
                dataType: 'text',
                async: false,
                success: function (res) {
                    dropzone.options.url = res;
                    dropzone.processFile(file);
                    
                }


            });
            console.error("after ajax");
        }

    });
});

//var input = $('[name="file"]'),
//    formdata = false;
//
//function showUploadedItem(source) {
//    var list = document.getElementById("image-list"),
//        li = document.createElement("li"),
//        img = document.createElement("img");
//    img.src = source;
//    li.appendChild(img);
//    list.appendChild(li);
//}
//
//
//
//if (window.FormData) {
//    formdata = new FormData();
////            document.getElementById("btn").style.display = "none";
//}
//input.addEventListener("change", function (evt) {
//     window.alert("a change");
//    document.getElementById("response").innerHTML = "Uploading . . .";
//    var i = 0, len = dropzone.files.length, img, reader, file;
//
//    for (; i < len; i++) {
//        file = this.files[i];
//
//        if (!!file.type.match(/image.*/)) {
//            if (window.FileReader) {
//                reader = new FileReader();
//                reader.onloadend = function (e) {
//                    showUploadedItem(e.target.result, file.fileName);
//                };
//                reader.readAsDataURL(file);
//            }
//            if (formdata) {
//                formdata.append("images[]", file);
//            }
//        }
//    }
//
//    if (formdata) {
//
//        $.ajax({
//            url: "{{upload_url}}",
//            type: "POST",
//            data: formdata,
//            processData: false,
//            contentType: false,
//            success: function (res) {
//                dropzone.options.url = res;
////                        $("#my-awesome-dropzone").attr(action, res);
//                for (file in dropzone.files)
//                    dropzone._processFile(file);
//                //document.getElementById("response").innerHTML = res;
//            }
//
//
//        });
//    }
//
//}, false);


