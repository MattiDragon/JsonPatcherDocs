"use strict";

for (let link of document.querySelectorAll("#nav-sidebar > nav:first-child a")) {
    if (link.href === location.origin + location.pathname) {
        link.classList.add("current");
    }
}
