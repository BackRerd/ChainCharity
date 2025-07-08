// src/utils/animation.js
export function animateValue(obj, prop, start, end, duration) {
    let startTimestamp = null
    const step = (timestamp) => {
        if (!startTimestamp) startTimestamp = timestamp
        const progress = Math.min((timestamp - startTimestamp) / duration, 1)
        obj[prop] = Math.floor(progress * (end - start) + start)
        if (progress < 1) {
            window.requestAnimationFrame(step)
        }
    }
    window.requestAnimationFrame(step)
}