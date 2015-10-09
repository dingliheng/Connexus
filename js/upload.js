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

$(function () {
    $("#uploadBtn").click(function () {

        document.getElementById("response").innerHTML = "Uploading . . .";

        var i = 0, len = dropzone.files.length;
        window.alert(len);
        files = dropzone.files;

        //for (; i < len; i++) {
        //    console.error("before");
        //    file = dropzone.files[i];
        //    console.error("after");
        //    $.ajax({
        //        url: window.location.href,
        //        type: "POST",
        //        dataType: 'text',
        //        async: false,
        //        success: function (res) {
        //            dropzone.options.url = res;
        //            dropzone.processFile(file);
        //
        //
        //        }
        //    });
        //    console.error("after ajax");
        //}

        //$.ajax({
        //    url: window.location.href,
        //    type: "POST",
        //    dataType: 'text',
        //    async: false,
        //    success: function (res) {
        //        dropzone.options.url = res;
        //        dropzone.processFile(files[0]);
        //        i++;
        //    }
        //});
         $.ajax({
                    url: window.location.href,
                    type: "POST",
                    dataType: 'text',
                    async: false,
                    success: function (res) {
                        dropzone.options.url = res;
                        dropzone.processFile(files[0]);
                        i++;
                    }
                });

        dropzone.on("complete", function() {
            if (i < len) {
                $.ajax({
                    url: window.location.href,
                    type: "POST",
                    dataType: 'text',
                    async: false,
                    success: function (res) {
                        dropzone.options.url = res;
                        dropzone.processFile(files[i]);
                        i++;
                    }
                });
            }
        });



    });

});

