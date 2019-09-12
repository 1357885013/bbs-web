// JavaScript Document
/*var c=document.getElementById("myCanvas").getContext("2d");
debug=c.beginPath();
c.rect(0,0,200,200);

c.moveTo(10,10);
c.lineTo(150,50);
c.lineTo(10,50);
c.resetTransform();

c.stroke();*/

//alert(c.save());
//alert(debug);
var preX,preY,preSlop;
var textAll="";
positions=new Array();
var i=0;

var canvas = document.getElementById("myCanvas");
var ctx = canvas.getContext("2d");


function drawCircle(x,y)
{
	var s = document.getElementById("mySvg");
	var ele = document.createElementNS('http://www.w3.org/2000/svg',"circle");
	ele.setAttribute("cx",x);
	ele.setAttribute("cy",y);
	ele.setAttribute("r",2);
	ele.setAttribute("stroke","black");
	ele.setAttribute("stroke-width",2);
	ele.setAttributeNS(null,"fill","red")
	//ele.setAttribute("fill","red");
	s.appendChild(ele);

}

function beginDraw(e)
{
	ctx.beginPath();
    ctx.moveTo(e.clientX-canvas.offsetLeft,e.clientY-canvas.offsetTop);
	
	temp=new Object();
	temp.x=e.clientX;
	temp.y=e.clientY;
	positions[i]=temp;i++;
	drawCircle(temp.x,temp.y-58);
	document.getElementById("mySvg").addEventListener("mousemove",drawing);
	document.getElementById("mySvg").addEventListener("mouseup",endDraw);

}
function endDraw(e)
{
	ctx.closePath();
	
	temp=new Object();
	temp.x=e.clientX;
	temp.y=e.clientY;
	positions[i]=temp;
	drawCircle(temp.x,temp.y-58);
	document.getElementById("mySvg").removeEventListener("mousemove",drawing);
	document.getElementById("mySvg").removeEventListener("mouseup",endDraw);

	
	//clearShowLocation()
}
//document.getElementById("mySvg").addEventListener("mousedown",beginDraw);


function drawing(e)
{
	
	ctx.lineTo(e.clientX - canvas.offsetLeft,e.clientY - canvas.offsetTop);
    ctx.stroke();
	
	var slop,dSlop;
	if(preX)
	{
		if(Math.abs(preX-e.clientX)<3||Math.abs(preY-e.clientY)<3)
			return;
		slop=Math.atan((preY-e.clientY)/(preX-e.clientX));
	}
	if(preSlop)
		dSlop=preSlop-slop;
	preX=e.clientX;
	preY=e.clientY;
	preSlop=slop;
	if(Math.abs(dSlop)>0.2)
		drawCircle(preX,preY-60)
	var p=document.getElementById("showLocation");
	var text="坐标X:"+e.clientX+"   Y:"+e.clientY+"角度："+slop+"d角度："+dSlop+"\n";
	textAll+=text;
	p.innerHTML=text;
}

function clearShowLocation()
{
	//alert(textAll);
	textAll="";
	var p=document.getElementById("showLocation");
	p.innerHTML="";
}



