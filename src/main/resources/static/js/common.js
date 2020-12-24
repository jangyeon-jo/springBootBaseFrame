///////////////////////////////////////////////////////////////////////////
// 공통 자바스크립트
//
// HISTORY
// 2017-11-24  시   헌   - 최초생성
///////////////////////////////////////////////////////////////////////////
// 디버그 전용 상수.
var KbdfDebug = true;

//-------------------------------------------------------------------
// submit
//-------------------------------------------------------------------
var KbdfAction = {};
//한번만 submit 하기 위한 전역변수
KbdfAction.isSubmit = false;

KbdfAction.submitForm = function (form, action, target){
    if(KbdfDebug){ console.log("submit check: "+ KbdfAction.isSubmit); }
    if(!KbdfAction.isSubmitted()){
        if(!KbdfUtil.isNull(action)){
            form.action = action;
        }

        if(!KbdfUtil.isNull(target)){
            form.target = target;
            KbdfAction.submitClear();
        }

        //데이타를 서버로 전송한다.
        form.submit();
        return;
    }
};
KbdfAction.isSubmitted = function (){
    if(KbdfAction.isSubmit){
        KbdfMessage.alert("처리중입니다.");
        return true;
    }else{
        KbdfAction.isSubmit = true;
        KbdfAction.showLoading();
        return false;
    }
}
KbdfAction.submitClear = function(){
    KbdfAction.isSubmit = false;
    KbdfAction.hideLoading();
}
KbdfAction.showLoading= function()
{
    var container = $('#blindContainer');
    if(container.length <= 0)
    {
        var loadingHTML = "<div id=\"blindContainer\" class=\"blindContainer\" style=\"display: none;\">"
                        + "	 <div class=\"blind\"></div>"
                        + "  <img src='./doc/image/underloading.gif' id='viewLoading' class=' style='display: none;' />"
                        + "</div>";
        $('body').append(loadingHTML);
    }
    $('#blindContainer').show();
    $('#viewLoading').show();
}
KbdfAction.hideLoading = function()
{
    $('#blindContainer').hide();
    $('#viewLoading').hide();
}

//-------------------------------------------------------------------
// 쿠키 유틸
//-------------------------------------------------------------------
var KbdfCookie = {};
KbdfCookie.set = function(name, value, expiredays){
    var today = new Date();
    today.setDate(today.getDate() + expiredays );
    document.cookie = name + "=" + escape( value ) + ";path=/;expires=" + today.toGMTString() + ";";
}

KbdfCookie.get = function(sName){
    //cookies are separated by semicolons
    var aCookie = document.cookie.split("; ");
    for(var i=0; i < aCookie.length; i++){
        // a name/value pair (a crumb) is separated by an equal sign
        var aCrumb = aCookie[i].split("=");
        if (sName == aCrumb[0]) {
            if (typeof(aCrumb[1]) == "undefined" || aCrumb[1] =="" || aCrumb[1] == null) return null;
                return unescape(aCrumb[1]);
        }
    }
    //a cookie with the requested name does not exist
    return null;
};

//-------------------------------------------------------------------
// 유틸
//-------------------------------------------------------------------
var KbdfUtil = {};
KbdfUtil.isNull = function(obj){
    if(null == obj || '' == obj || 'undefined' == typeof(obj))
        return true;
    return false;
};
KbdfUtil.getFormJson = function(formObj){
    var o = {};
    var a = formObj.serializeArray();
    $.each(a, function() {
        if (null!=o[this.name] && 'undefined'!=o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
}

//-------------------------------------------------------------------
// CSRF 유틸
//-------------------------------------------------------------------
var KbdfCsrfUtil = {};
KbdfCsrfUtil.token = $("meta[name='_csrf']").attr("content");
KbdfCsrfUtil.header = $("meta[name='_csrf_header']").attr("content");
// csrf
$(document).ajaxSuccess(function(e, xhr, settings) {
    if(KbdfDebug){
        console.log("check beforesend");
        console.log(settings.type, !/^(GET|HEAD|OPTIONS|TRACE)$/i.test(settings.type));
    }
    if (!/^(GET|HEAD|OPTIONS|TRACE)$/i.test(settings.type)) {
        console.log("xhr >>>>>>>", xhr);
        xhr.setRequestHeader(KbdfCsrfUtil.header, KbdfCsrfUtil.token);
    }
});
KbdfCsrfUtil.setAngularHeader= function(headers){
    headers['Content-Type']= 'application/x-www-form-urlencoded;charset=utf-8;';
    headers[KbdfCsrfUtil.header]=KbdfCsrfUtil.token;
}

//-------------------------------------------------------------------
// 팝업유틸
//-------------------------------------------------------------------
var KbdfMessage = {
    open : function(option){
        if(KbdfDebug) {
            console.log("KbdfMessage option",option);
        }
        if(KbdfUtil.isNull(option.layer_id)=="undefined") { if(KbdfDebug){console.log('KbdfMessage required field is null : layer_id');} return; }
        if(KbdfUtil.isNull(option.target_id)=="undefined") { if(KbdfDebug){console.log('KbdfMessage required field is null : target_id');} return; }
        if(KbdfUtil.isNull(option.scroll)=="undefined") { option.scroll = "1"; }
        //$("#target2").length>0 | document.getElementById("target2")==null
        if(document.getElementById(option.target_id)==null){
            $("<div id=\""+option.layer_id+"\"><iframe id=\""+option.target_id+"\" name=\""+option.target_id+"\" src=\""+((option.src!=null && option.src !="")? option.src:"about:blank") +"\" marginWidth=0 marginHeight=0 frameborder=0 width=\"100%\" height=\"100%\" scrolling="+option.scroll+"></iframe></div>").appendTo($('body'));
        }else{
            // 초기화
            document.getElementById(option.target_id).src=option.src;
        }

        $("#"+option.layer_id).dialog({
            modal: true,
            resizable: false,
            //position:['right','center'],
            width: (KbdfUtil.isNull(option.width)? '80%':option.width),
            height: (KbdfUtil.isNull(option.height)? '500':option.height),
            title: ((option.title!=null && option.title !="")? option.title:"untitled!!")
        });
        // close 버튼에 아무것도 안보여서 추가
        $.each($("button.ui-dialog-titlebar-close"), function(idx, obj){
            var iconObj = $(obj).find("> i");
            if(iconObj.length < 1){
                $("<i class='fa fa-window-close'></i>").appendTo($(obj));
            }
        });
    },
    close : function(p_layer_id){
        $("#"+p_layer_id).dialog("close");
    },
    alert: function(msg, title, callback){
        if(KbdfMessage.isConfirmCall){
            alert('이전 요청을 먼저 확인해주세요!');
            return;
        }
        KbdfMessage.isConfirmCall = true;
        // check
        if(KbdfUtil.isNull(title)){
            title = '확인';
        }
        if(!KbdfUtil.isNull(callback)){
            KbdfMessage.alertCallback = callback;
       }
        $("#kb-pop-div .panel-heading").html(title);
        $("#kb-pop-div .panel-body").html(msg);
        $("#kb-pop-div .kb-alert").show();
        $("#kb-pop-div").show();
         $("div.kb-bg").css("height",$("html").css("height"));
        $("div.kb-bg").show();
        $(window).scrollTop(0);
    },
    closeAlert: function(callObj){
        KbdfMessage.isConfirmCall = false;
        $("#kb-pop-div .panel-heading span").html('');
        $("#kb-pop-div .panel-body").html('');
        $("#kb-pop-div").hide();
        $("#kb-pop-div .kb-alert").hide();
        $("div.kb-bg").hide();
        // reset
        if(null!=this.alertCallback && $(callObj).hasClass('kb-btn-ok')) {
            $("#kb-pop-div .kb-alert.kb-btn-ok").unbind('click',KbdfMessage.alertCallback);
            KbdfMessage.alertCallback();
            KbdfMessage.alertCallback = function(){};
        }
    },
    confirm: function(msg, title, callback){
        if(KbdfMessage.isConfirmCall){
            alert('이전 요청을 먼저 확인해주세요!');
            return;
        }
        // check
        if(KbdfUtil.isNull(title)){
            title = '질문';
        }
        if(!KbdfUtil.isNull(callback)){
            KbdfMessage.confirmCallback = callback;
        }

        $("#kb-pop-div .panel-heading span").html(title);
        $("#kb-pop-div .panel-body").html(msg);
        $("#kb-pop-div").show();
        $("#kb-pop-div .kb-confirm").show();
        $("div.kb-bg").css("height",$("html").css("height"));
        $("div.kb-bg").show();
        $(window).scrollTop(0);
    },
    closeConfirm: function(callObj){
        KbdfMessage.isConfirmCall = false;
        $("#kb-pop-div .panel-heading span").html('');
        $("#kb-pop-div .panel-body").html('');
        $("#kb-pop-div").hide();
        $("#kb-pop-div .kb-confirm").hide();
        $("div.kb-bg").hide();
        // reset
        if(null!=this.confirmCallback && $(callObj).hasClass('kb-btn-ok')) {
            $("#kb-pop-div .kb-confirm.kb-btn-ok").unbind('click',KbdfMessage.confirmCallback);
            KbdfMessage.confirmCallback();
            KbdfMessage.confirmCallback = function(){};
        }
    },
    /** 중복방지 */
    isConfirmCall:false,
    /** confirm 콜백 */
    confirmCallback: null,
    /** alert 콜백 */
    alertCallback: null,
    toast: function(type, title, message){
        if(KbdfUtil.isNull(title)){
            toastr[type](message);
        }
        else {
            toastr[type](message, title);
        }
    },
    initToast: function(){
        if(KbdfUtil.isNull(toastr)){
            return;
        }
        // toastr.
        toastr.options = {
          "closeButton": true,
          "debug": false,
          "progressBar": true,
          "preventDuplicates": false,
          "positionClass": "toast-top-right",
          "showDuration": "400",
          "hideDuration": "1000",
          "timeOut": "7000",
          "extendedTimeOut": "1000",
          "showEasing": "swing",
          "hideEasing": "linear",
          "showMethod": "fadeIn",
          "hideMethod": "fadeOut"
        };
    }
};

//-------------------------------------------------------------------
// ajax
//-------------------------------------------------------------------
var KbdfAjax = {};
/**
 * 공통 ajax 객체
 */
KbdfAjax.ajax = function(form, target_url, options){
    if(KbdfUtil.isNull(target_url)){
        var errMsg = i18n('error.isnull',['target_url']);
        KbdfMessage.alert(errMsg);
        return;
    }
    var tmpOptions = $.extend(true,
        {
            url: target_url,
            async: true, // false 일 경우 동기 요청으로 변경
            type: 'POST',
            data: KbdfUtil.getFormJson($(form)),
            contentType: 'application/x-www-form-urlencoded; charset=utf-8',
            error: function(jqXHR) {
                console.log('error',jqXHR);
            }, // 요청 실패.
            complete: function(jqXHR) {
            } // 요청의 실패, 성공과 상관 없이 완료 될 경우 호출
        },
        options || {}
    );
    $.ajax(tmpOptions);
};

KbdfAjax.hasError = function(resultMsg){
    var returnJson = { hasError: false, errorMsg: null };
    if(KbdfUtil.isNull(resultMsg)) {
        returnJson.hasError = true;
        returnJson.errorMsg = 'response is null!';
    }
    else if('string' == typeof(resultMsg)) {
        if(resultMsg.indexOf('ErrorPage start') > -1){
            returnJson.hasError = true;

            var startIdx = resultMsg.indexOf('ErrorPage start')+("ErrorPage start".length+1);
            var endIdx = resultMsg.indexOf('ErrorPage end');
            if(endIdx-startIdx > 1000) endIdx = startIdx+1000;
            returnJson.errorMsg = resultMsg.substring(startIdx,  endIdx);
        }
    }
    return returnJson;
}

//loading start. 모든 ajax 요청 시작부분에서 실행됨.
$(document).ajaxStart(function(){
    KbdfAction.showLoading();
});
//loading end. 모든 ajax 요청이 종료되면 실행됨.
$(document).ajaxStop(function(){
    KbdfAction.hideLoading();
});
//-------------------------------------------------------------------
// body onload
//-------------------------------------------------------------------
$(document).ready(function(){
    // toast
    KbdfMessage.initToast();

    // title 설정
    //if($("#page-wrapper .page-heading H3").length > 0 && top == self ){
    //    document.title = $("#page-wrapper .page-heading H3").text() +" "+ document.title;
    //}

    if(KbdfDebug){
        console.log('common.js loading complete.');
    }

	// 토글 기능    
    $('.btn-toggle').click(function() {
        $(this).find('.btn').toggleClass('active');  
        
        if (0 < $(this).find('.btn-primary').length) {
        	$(this).find('.btn').toggleClass('btn-primary');
        }
        if (0 < $(this).find('.btn-danger').length) {
        	$(this).find('.btn').toggleClass('btn-danger');
        }
        if (0 < $(this).find('.btn-success').length) {
        	$(this).find('.btn').toggleClass('btn-success');
        }
        if (0 < $(this).find('.btn-info').length) {
        	$(this).find('.btn').toggleClass('btn-info');
        }
        if (0 < $(this).find('.btn-kb').length) {
        	$(this).find('.btn').toggleClass('btn-kb');
        }
        
        $(this).find('.btn').toggleClass('btn-default');
           
    });
});