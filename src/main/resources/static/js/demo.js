/*
* 提交评论
* */
function commentTarget1() {
    var questionId = $("#QuestionId").val();
    var content = $("#text_demo").val();
    post(questionId,content,1);

}

function commentOnLode() {
    $("#comment_text").hide();
}

function comment() {
    $("#comment_text").show();
}
function addLikeCount(e) {
    var id = e.getAttribute("data-like");
    var hasClass = $("#" + id).hasClass("active");
    if (hasClass) {
        e.classList.remove("active");
    } else {
        e.classList.add("active");
    }
}
/*
* 展开二级评论
* */
function showCollapse(e) {
    var id = e.getAttribute("data-id");
    var hasClass = $("#comment-" + id).hasClass("in");
    if (hasClass) {
        //折叠二级评论
        $("#comment-" + id).removeClass("in");
        e.classList.remove("active");
    } else {
        //展开二级评论
        var commentContent = $("#comment-" + id);
        if (commentContent.children().length != 1) {
            $("#comment-"+id).addClass("in");
            e.classList.add("active");
        } else {
            $.getJSON("/comment/"+id , function (data) {
                $.each(data.data.reverse(), function (index, comment) {
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-circle",
                        "src":comment.user.avatarUrl
                    }));
                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h4/>", {
                        "class": "media-heading",
                        "html":comment.user.name
                    })).append($("<div/>", {
                        "html":comment.comment.content
                    })).append($("<div/>", {
                    })).append($("<span/>", {
                        "class": "glyphicon glyphicon-comment demo-menu"
                    })).append($("<span/>", {
                        "class": "demo-menu-2",
                        "html":moment(comment.comment.gmtCreate).format('YYYY年MM月DD日 HH:mm')
                    }));
                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement)
                        .append(mediaBodyElement);
                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comment"
                    }).append(mediaElement);
                    commentContent.prepend(commentElement);
                });
                $("#comment-"+id).addClass("in");
                e.classList.add("active");
            });
        }
    }
}

function post(questionId,content,type) {
    if(!content){
        alert("评论的内容不能为空");
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        data: JSON.stringify({
            "parentId": questionId,
            "content": content,
            "type": type
        }),
        contentType: "application/json",
        dataType: "json",
        success: function (data) {
            if (data.success == 200) {
                window.location.reload();
            } else {
                if (data.success == 2003){
                    var isAccepted = confirm(data.message);
                    if(isAccepted){
                        window.open("https://github.com/login/oauth/authorize?client_id=8c737e6d3072feb41c44&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        window.localStorage.setItem("closable",true);
                    }
                }else {
                    alert(data.message);
                }
            }

        }
    });

}

function commentTarget2(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-"+commentId).val();
    post(commentId,content,2);
}

function selectTag(e) {
    var value = e.getAttribute("data-tag");
    var inputTag = $("#tag").val();
    if(inputTag.indexOf(value) == -1){
        if(inputTag){
            $("#tag").val(inputTag+','+value);
        }else {
            $("#tag").val(value);
        }
    }
}

function showSelectTag() {
    $("#select-tag").show();
}