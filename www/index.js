/* activate sidebar */
$('#sidebar').affix({
  offset: {
    top: 235
  }
});

/* activate scrollspy menu */
var $body   = $(document.body);
var navHeight = $('.navbar').outerHeight(true) + 10;

$body.scrollspy({
	target: '#leftCol',
	offset: navHeight
});

function hashFunction() {
  var hash = "qiyh4XPJGsOZ2MEAyLkfWqeQ";
  var hashCode = "#";
  hashCode = hashCode.concat(hash);
  var hButton = document.getElementById("hashButton");
  document.getElementById("hashCode").innerHTML = hash;
  hButton.href = hashCode;
  window.location.href=hashCode;
  hButton.innerHTML = "This is now your hash:";
  hButton.disabled = true;
}

/* smooth scrolling sections */
$('a[href*=#]:not([href=#])').click(function() {
    if (location.pathname.replace(/^\//,'') == this.pathname.replace(/^\//,'') && location.hostname == this.hostname) {
      var target = $(this.hash);
      target = target.length ? target : $('[name=' + this.hash.slice(1) +']');
      if (target.length) {
        $('html,body').animate({
          scrollTop: target.offset().top - 50
        }, 1000);
        return false;
      }
    }
});