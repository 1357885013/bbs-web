

//验证信息
myValid={
    //验证客户端所有要提交的信息
    rules: {
        username: {
            required: true,
            minlength: 4,
            maxlength:25
        },
        name:{
            required:true,
            minlength: 2,
            maxlength:8
        },
        jobNum:{
            required:true,
            minlength: 2,
            maxlength:18,
            number:true
        },
        stuNum:{
            required:true,
            minlength: 2,
            maxlength:18,
            number:true
        },
        school:{
            required:true,
            minlength: 2,
            maxlength:18
        },
        institute:{
            required:true,
            minlength: 2,
            maxlength:18
        },
        sex:{
            required:true,
        },
        email: {
            required: true,
            email: true
        },

        password: {
            required: true,
            minlength: 6
        },
        rePassword: {
            required: true,
            minlength: 6,
            equalTo: "#password"
        },
        captcha:{
            required:true,
            minlength: 2,
            maxlength:18
        },
        agree: "required"
    },
    messages: {
        rePassword: {
            equalTo: "两次输入的密码不一样"
        },
    },
    errorElement: "label",
    errorPlacement: function ( error, element ) {
        // errorBox类名里定义者展示错误信息标签的样式
        error.addClass( "errorBox" );

        // 添加`has-feedback`类名给输入框的parents用来给对错号定位
        element.parents( ".form-group" ).addClass( "has-feedback" );

        if ( element.prop( "type" ) === "radio" ) {
            //error.before( element.parent( "label" ) );
            element.parent().parent().before(error);
        } else {
            element.before(error);
        }

        // 添加错号图标
        if ( !element.next( "span" )[ 0 ] ) {
            $( "<span class='glyphicon glyphicon-remove form-control-feedback'></span>" ).insertAfter( element );
        }
    },
    success: function ( label, element ) {
        // 添加对号图标
        if ( !$( element ).next( "span" )[ 0 ] ) {
            $( "<span class='glyphicon glyphicon-ok form-control-feedback'></span>" ).insertAfter( $( element ) );
        }
    },
    highlight: function ( element, errorClass, validClass ) {
        $( element ).parents( ".form-group" ).addClass( "has-error" ).removeClass( "has-success" );
        $( element ).next( "span" ).addClass( "glyphicon-remove" ).removeClass( "glyphicon-ok" );
    },
    unhighlight: function ( element, errorClass, validClass ) {
        $( element ).parents( ".form-group" ).addClass( "has-success" ).removeClass( "has-error" );
        $( element ).next( "span" ).addClass( "glyphicon-ok" ).removeClass( "glyphicon-remove" );
    }
};

