/**
 * Created by Administrator on 2015/11/24.
 */
$(function(){
	//过期时间提示
	$('[the-id=no_show]').click(function(){
        $('[the-id=waringpub_title]').fadeOut();
    });
	
    //设置左边导航的背景高度
   // var Height = $(window).height()-50;
    //    $('.nav_wrap').height(Height);
    //导航交互
   /* $('[the-id=nav_list] li').hover(function(){
        var _this = $(this);
        if(!_this.hasClass('hover')){
            $(this).children('span').toggleClass('hover');
        }
    });
    $('[the-id=nav_list] li').click(function(){
        var _this = $(this);
        _this.addClass('hover').siblings().removeClass('hover');
        _this.siblings().children('span').removeClass('hover');
        if(_this.hasClass('hover')){
            _this.children('span').addClass('hover');
        };
    });
    */
    /*选项卡
    $('[the-id=arc_changenav] li').click(function(){
        $(this).addClass('hover').siblings().removeClass('hover');
        $('[the-id=arc_changecon] > div').eq($(this).index()).show().siblings().hide();
    });
    */
	
    //登录页渐入效果
    setInterval(function(){
        $('[the-id=text_one]').animate({left:0},1000);
        $('[the-id=text_one]').fadeIn(4000,function(){
            $('[the-id=text_two]').fadeIn(4000);
        });
    },2000);
    //输入框鼠标经过
  //  $(function(){
        $('[the-id=name_input]').hover(function(){
            $('[the-id=name_img]').attr('src','${ctx}/style/images/man_pichover.png');
            $('[the-id=logininput_text]').css('border-color','#1b80ba');
        },function(){
            $('[the-id=name_img]').attr('src','${ctx}/style/images/man_pic.png');
            $('[the-id=logininput_text]').css('border-color','#bbc3d3');
        });
        $('[the-id=password_input]').hover(function(){
            $('[the-id=pass_img]').attr('src','${ctx}/style/images/pass_pichover.png');
            $('[the-id=logininput_pass]').css('border-color','#1b80ba');
        },function(){
            $('[the-id=pass_img]').attr('src','${ctx}/style/images/pass_pic.png');
            $('[the-id=logininput_pass]').css('border-color','#bbc3d3');
        });
   // });
       /*
        //执行任务回复贴添加内容开始
        var replt_html = '',
            $olcon = $('[the-id=replyappend_ul]'),
             appendsonhtml = $('[the-id=replyappend_ul]').html();
            replt_html += appendsonhtml;
            $('[the-id=reply_append]').click(function(){
                $olcon.append(replt_html);
            });
        $('[the-id=reply_close]').live('click',function(){
            $(this).parents('li').remove().empty();
        });
        //执行任务回复贴添加内容结束
        //执行任务回复贴添加地址开始
        var replt_addresshtml = '',
            $olcontent = $('[the-id=replyappend_uladd]'),
            urlsonhtml = $('[the-id=replyappend_uladd]').html();
        replt_addresshtml += urlsonhtml;
        $('[the-id=append_address]').click(function(){
            //var length = $('[the-id=replyappend_uladd] li').length;
            //if(length>=5){
            //    alert('最多添加五条url');
            //    return;
            //}
            $olcontent.append(replt_addresshtml);
        });
        $('[the-id=replyadd_close]').live('click',function(){
            $(this).parents('li').remove().empty();
        });
        //执行任务回复贴添加地址结束
*/        
/*
        //执行任务点赞切换选项卡开始
        $('[the-id=click_navlist] li').click(function(){
            $(this).addClass('hover').siblings().removeClass('hover');
            $('[the-id=big_clickdiv] > div').eq($(this).index()).show().siblings().hide();
        });
        //执行任务点赞切换选项卡结束
*/
});