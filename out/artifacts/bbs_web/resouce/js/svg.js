
started=false;
svg=document.getElementById("mySvg");
canvas = document.getElementById("myCanvas");
infoBox=document.getElementById("infoBox");
svgEle=new Object;
dAttr=new Object;
/*<polyline fill="none" stroke="#000" stroke-width="1.5" opacity="0.5" style="pointer-events:none" points="132,89.4375 133,89.4375 142,93.4375 157,102.4375 175,108.4375 195,114.4375 217,116.4375 246,116.4375 282,113.4375 314,108.4375 353,104.4375 378,104.4375 380,104.4375 381,104.4375 382,104.4375 " id="svg_1" stroke-linecap="round"></polyline>

<path fill="none" stroke="#000" stroke-width="1.5" style="pointer-events:inherit" id="svg_1" d="M132,89.4375 C 133,89.4375,141.10751342773438,95.12799072265625,157,102.4375 174.23782348632812,110.36577606201172,194.93197631835938,115.43647003173828,217,116.4375 245.97021484375,117.75161743164062,281.8791809082031,112.59121704101562,314,108.4375 352.880859375,103.40960693359375,378,104.4375,380,104.4375 L 381,104.4375 382,104.4375" stroke-dasharray="none"></path>*/
function assignAttributes(ele,attr)
{
	//b.numberOfItems;
	for(var eachName in attr)
		{
			if(attr[eachName])
				ele.setAttribute(eachName,attr[eachName]);
		}
}

function addSVGElementFromJson(json) {
        
      var ele = document.createElementNS('http://www.w3.org/2000/svg', json.element);
      var J=json.attr;
        json.curStyles && assignAttributes(ele, {
            fill: J.fill,
            stroke: J.stroke,
            "stroke-width": J.stroke_width,
            "stroke-dasharray": J.stroke_dasharray,
            "stroke-opacity": J.stroke_opacity,
            "fill-opacity": J.fill_opacity,
            opacity: J.opacity / 2,
            style: "pointer-events:inherit"
        }, 100);
        assignAttributes(ele, json.attr, 100);
        return ele;
    }
/**
 * @typedef {PlainObject} Point
 * @property {Integer} x The x value
 * @property {Integer} y The y value
 */

/**
* Takes three points and creates a smoother line based on them.
* @function module:path.smoothControlPoints
* @param {Point} ct1 - Object with x and y values (first control point)
* @param {Point} ct2 - Object with x and y values (second control point)
* @param {Point} pt - Object with x and y values (third point)
* @returns {Point[]} Array of two "smoothed" point objects
*/
function smoothControlPoints(ct1, ct2, pt) {
  // each point must not be the origin
  const x1 = ct1.x - pt.x,
    y1 = ct1.y - pt.y,
    x2 = ct2.x - pt.x,
    y2 = ct2.y - pt.y;

  if ((x1 !== 0 || y1 !== 0) && (x2 !== 0 || y2 !== 0)) {
    const
      r1 = Math.sqrt(x1 * x1 + y1 * y1),
      r2 = Math.sqrt(x2 * x2 + y2 * y2),
      nct1 = svg.createSVGPoint(),
      nct2 = svg.createSVGPoint();
    let anglea = Math.atan2(y1, x1),
      angleb = Math.atan2(y2, x2);
    if (anglea < 0) { anglea += 2 * Math.PI; }
    if (angleb < 0) { angleb += 2 * Math.PI; }

    const angleBetween = Math.abs(anglea - angleb),
      angleDiff = Math.abs(Math.PI - angleBetween) / 2;

    let newAnglea, newAngleb;
    if (anglea - angleb > 0) {
      newAnglea = angleBetween < Math.PI ? (anglea + angleDiff) : (anglea - angleDiff);
      newAngleb = angleBetween < Math.PI ? (angleb - angleDiff) : (angleb + angleDiff);
    } else {
      newAnglea = angleBetween < Math.PI ? (anglea - angleDiff) : (anglea + angleDiff);
      newAngleb = angleBetween < Math.PI ? (angleb + angleDiff) : (angleb - angleDiff);
    }

    // rotate the points
    nct1.x = r1 * Math.cos(newAnglea) + pt.x;
    nct1.y = r1 * Math.sin(newAnglea) + pt.y;
    nct2.x = r2 * Math.cos(newAngleb) + pt.x;
    nct2.y = r2 * Math.sin(newAngleb) + pt.y;

    return [nct1, nct2];
  }
  return undefined;
};


function smoothPolylineIntoPath(element) {
    let i;
    const {points} = element;
    const N = points.numberOfItems;
    if (N >= 4) {
      // loop through every 3 points and convert to a cubic bezier curve segment
      //
      // NOTE: this is cheating, it means that every 3 points has the potential to
      // be a corner instead of treating each point in an equal manner. In general,
      // this technique does not look that good.
      //
      // I am open to better ideas!
      //
      // Reading:
      // - http://www.efg2.com/Lab/Graphics/Jean-YvesQueinecBezierCurves.htm
      // - https://www.codeproject.com/KB/graphics/BezierSpline.aspx?msg=2956963
      // - https://www.ian-ko.com/ET_GeoWizards/UserGuide/smooth.htm
      // - https://www.cs.mtu.edu/~shene/COURSES/cs3621/NOTES/spline/Bezier/bezier-der.html
      let curpos = points.getItem(0), prevCtlPt = null;
      let d = [];
      d.push(['M', curpos.x, ',', curpos.y].join(''));
      for (i = 1; i <= (N - 4); i += 3) {
        let ct1 = points.getItem(i);
        const ct2 = points.getItem(i + 1);
        const end = points.getItem(i + 2);

        // if the previous segment had a control point, we want to smooth out
        // the control points on both sides
        if (prevCtlPt) {
          const newpts = smoothControlPoints(prevCtlPt, ct1, curpos);
          if (newpts && newpts.length === 2) {
            const prevArr = d[d.length - 1].split(',');
            prevArr[2] = newpts[0].x;
            prevArr[3] = newpts[0].y;
            d[d.length - 1] = prevArr.join(',');
            ct1 = newpts[1];
          }
        }
		const n=5;
		const n=5;
        d.push([' C',ct1.x.toFixed(n),',', ct1.y.toFixed(n),' ', ct2.x.toFixed(n),',', ct2.y.toFixed(n),'   ', end.x.toFixed(n),',', end.y.toFixed(n)].join(''));

        curpos = end;
        prevCtlPt = ct2;
      }
      // handle remaining line segments
      d.push('L');
      while (i < N) {
        const pt = points.getItem(i);
        d.push([pt.x, pt.y].join(','));
        i++;
      }
      d = d.join(' ');

      // create new path element
      element = addSVGElementFromJson({
        element: 'path',
        curStyles: true,
        attr: {
          id: "fhpath_1",
          d,
          fill: 'none'
        }
      });
      // No need to call "changed", as this is already done under mouseUp
    }
    return element;
  };


const mouseDown = function (evt) {
 

	const realX=evt.pageX-svg.getBoundingClientRect().left;
	const realY=evt.pageY-svg.getBoundingClientRect().top;	
	
	var start=new Object();
	
	start.x = realX;
    start.y = realY;
    started = true;
    dAttr = realX + ',' + realY + ' ';
    // Commented out as doing nothing now:
    // strokeW = parseFloat(curShape.stroke_width) === 0 ? 1 : curShape.stroke_width;
    svgEle=addSVGElementFromJson({
      element: 'polyline',
      curStyles: true,
      attr: {
		'stroke':"#000000",
		'stroke-width':3,
        'points': dAttr,
        'id': 'svg_1',
        'fill': 'none',
        'opacity': 1,
        'stroke-linecap': 'round',
        'style': 'pointer-events:none'
      }
    });
	svg.appendChild(svgEle);

    
	svg.addEventListener('mousemove', mouseMove);

	
}



const getBsplinePoint = function (t) {
  const spline = {x: 0, y: 0},
    p0 = controllPoint2,
    p1 = controllPoint1,
    p2 = start,
    p3 = end,
    S = 1.0 / 6.0,
    t2 = t * t,
    t3 = t2 * t;

  const m = [
    [-1, 3, -3, 1],
    [3, -6, 3, 0],
    [-3, 0, 3, 0],
    [1, 4, 1, 0]
  ];

  spline.x = S * (
    (p0.x * m[0][0] + p1.x * m[0][1] + p2.x * m[0][2] + p3.x * m[0][3]) * t3 +
      (p0.x * m[1][0] + p1.x * m[1][1] + p2.x * m[1][2] + p3.x * m[1][3]) * t2 +
      (p0.x * m[2][0] + p1.x * m[2][1] + p2.x * m[2][2] + p3.x * m[2][3]) * t +
      (p0.x * m[3][0] + p1.x * m[3][1] + p2.x * m[3][2] + p3.x * m[3][3])
  );
  spline.y = S * (
    (p0.y * m[0][0] + p1.y * m[0][1] + p2.y * m[0][2] + p3.y * m[0][3]) * t3 +
      (p0.y * m[1][0] + p1.y * m[1][1] + p2.y * m[1][2] + p3.y * m[1][3]) * t2 +
      (p0.y * m[2][0] + p1.y * m[2][1] + p2.y * m[2][2] + p3.y * m[2][3]) * t +
      (p0.y * m[3][0] + p1.y * m[3][1] + p2.y * m[3][2] + p3.y * m[3][3])
  );

  return {
    x: spline.x,
    y: spline.y
  };
};



const mouseMove = function (evt) {
	evt=evt||window.event;
/*	const realX=evt.pageX-canvas.offsetLeft;
	const realY=evt.pageY-canvas.offsetTop;*/
	
	
	const realX=evt.pageX-svg.getBoundingClientRect().left;
	const realY=evt.pageY-svg.getBoundingClientRect().top;	
	
	
	dAttr += + realX + ',' + realY + ' ';
    svgEle.setAttribute('points', dAttr);//实时刷新，不用添加删除
	infoBox.innerHTML=dAttr;
/*    end.x = realX; end.y = realY;
    if (controllPoint2.x && controllPoint2.y) {
      for (i = 0; i < STEP_COUNT - 1; i++) {
        parameter = i / STEP_COUNT;
        nextParameter = (i + 1) / STEP_COUNT;
        bSpline = getBsplinePoint(nextParameter);
        nextPos = bSpline;
        bSpline = getBsplinePoint(parameter);
        sumDistance += Math.sqrt((nextPos.x - bSpline.x) * (nextPos.x - bSpline.x) + (nextPos.y - bSpline.y) * (nextPos.y - bSpline.y));
        if (sumDistance > THRESHOLD_DIST) {
          sumDistance -= THRESHOLD_DIST;

          // Faster than completely re-writing the points attribute.
          const point = svgcontent.createSVGPoint();
          point.x = bSpline.x;
          point.y = bSpline.y;
          shape.points.appendItem(point);
        }
      }
    }
    controllPoint2 = {x: controllPoint1.x, y: controllPoint1.y};
    controllPoint1 = {x: start.x, y: start.y};
    start = {x: end.x, y: end.y};*/
  // update path stretch line coordinates
}




const mouseUp = function (evt) {
	svg.removeEventListener('mousemove', mouseMove);
	const realX=evt.pageX-svg.getBoundingClientRect().left;
	const realY=evt.pageY-svg.getBoundingClientRect().top;	
	
	
  

  started = false;


		  
		// Check that the path contains at least 2 points; a degenerate one-point path
    // causes problems.
    // Webkit ignores how we set the points attribute with commas and uses space
    // to separate all coordinates, see https://bugs.webkit.org/show_bug.cgi?id=29870
    sumDistance = 0;
    controllPoint2 = {x: 0, y: 0};
    controllPoint1 = {x: 0, y: 0};
    start = {x: 0, y: 0};
    end = {x: 0, y: 0};
    const coords = svgEle.getAttribute('points');
    const commaIndex = coords.indexOf(',');
    if (commaIndex >= 0) {
      keep = coords.indexOf(',', commaIndex + 1) >= 0;
    } else {
      keep = coords.indexOf(' ', coords.indexOf(' ') + 1) >= 0;
    }
    if (keep) {
      svgEle = smoothPolylineIntoPath(svgEle);
    }
	
	svg.appendChild(svgEle);
	var child=document.getElementById("svg_1");
	child.parentNode.removeChild(child);
		  
  }



svg.addEventListener('mousedown', mouseDown);
svg.addEventListener('mouseup', mouseUp);
