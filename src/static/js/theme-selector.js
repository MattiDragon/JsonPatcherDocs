"use strict";

/**
 * @type {{[p: string]: HTMLInputElement}}
 */
const themeSelectors = Object.fromEntries(document.querySelectorAll("#theme-selector input").values().map(input => [input.value, input]))

let theme = localStorage.getItem("theme")
if (theme !== "light" && theme !== "dark" && theme !== "auto") {
    theme = "auto";
}

themeSelectors[theme].checked = true
document.documentElement.dataset.theme = theme

for (const key in themeSelectors) {
    themeSelectors[key].addEventListener("change", () => {
        if (themeSelectors[key].checked) {
            localStorage.setItem("theme", key)
            document.documentElement.dataset.theme = key
        }
    })
}
