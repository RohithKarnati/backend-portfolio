document.addEventListener("DOMContentLoaded", () => {

    const glow = document.querySelector(".cursor-glow")

    document.addEventListener("mousemove", e => {

        glow.style.left = e.clientX + "px"
        glow.style.top = e.clientY + "px"

    })


    document.querySelectorAll(".tech-card").forEach(card => {

        card.addEventListener("mousemove", e => {

            const rect = card.getBoundingClientRect()

            card.style.setProperty("--x", e.clientX - rect.left + "px")
            card.style.setProperty("--y", e.clientY - rect.top + "px")

        })

    })

})