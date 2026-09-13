(function () {
    "use strict";

    const HOME = "/home/rohith";
    const TERMINAL_ENDPOINT = "/api/developer/terminal";

    const output = document.getElementById("terminalOutput");
    const input = document.getElementById("terminalInput");
    const promptEl = document.getElementById("prompt");

    let currentPath = HOME;
    const commandHistory = [];
    let historyCursor = -1; // -1 means "not currently browsing history"

    function displayPath(path) {
        if (path === HOME) return "~";
        if (path.indexOf(HOME + "/") === 0) return "~" + path.slice(HOME.length);
        return path;
    }

    function updatePrompt() {
        promptEl.textContent = "rohith@portfolio:" + displayPath(currentPath) + "$";
    }

    function appendLine(text, className) {
        const line = document.createElement("pre");
        line.className = "line" + (className ? " " + className : "");
        line.textContent = text;
        output.appendChild(line);
        output.scrollTop = output.scrollHeight;
    }

    function appendEcho(commandLine) {
        appendLine("rohith@portfolio:" + displayPath(currentPath) + "$ " + commandLine, "line-echo");
    }

    function clearTerminal() {
        output.textContent = "";
    }

    function printWelcome() {
        appendLine("Welcome to Rohith's Developer Environment.");
        appendLine("Type 'help' to see available commands.");
        appendLine("");
    }

    function printHistory() {
        if (commandHistory.length === 0) {
            appendLine("(no history yet)");
            return;
        }
        const lines = commandHistory.map((cmd, i) => (i + 1) + "  " + cmd);
        appendLine(lines.join("\n"));
    }

    async function runOnBackend(commandLine) {
        try {
            const response = await fetch(TERMINAL_ENDPOINT, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ command: commandLine, path: currentPath })
            });

            if (!response.ok) {
                appendLine("terminal: unexpected server response (" + response.status + ")", "line-error");
                return;
            }

            const data = await response.json();
            if (data.output) {
                appendLine(data.output, data.type === "ERROR" ? "line-error" : "");
            }
            if (data.currentPath) {
                currentPath = data.currentPath;
                updatePrompt();
            }
        } catch (err) {
            appendLine("terminal: unable to reach server", "line-error");
        }
    }

    function handleSubmit(rawInput) {
        const trimmed = rawInput.trim();
        if (trimmed === "") {
            appendEcho("");
            return;
        }

        const firstWord = trimmed.split(/\s+/)[0].toLowerCase();

        if (firstWord === "clear") {
            commandHistory.push(trimmed);
            clearTerminal();
            return;
        }

        if (firstWord === "history") {
            appendEcho(trimmed);
            printHistory();
            commandHistory.push(trimmed);
            return;
        }

        appendEcho(trimmed);
        commandHistory.push(trimmed);
        runOnBackend(trimmed);
    }

    input.addEventListener("keydown", (e) => {
        if (e.key === "Enter") {
            e.preventDefault();
            const value = input.value;
            input.value = "";
            historyCursor = -1;
            handleSubmit(value);
            return;
        }

        if (e.key === "ArrowUp") {
            e.preventDefault();
            if (commandHistory.length === 0) return;
            historyCursor = historyCursor === -1 ? commandHistory.length - 1 : Math.max(0, historyCursor - 1);
            input.value = commandHistory[historyCursor];
            return;
        }

        if (e.key === "ArrowDown") {
            e.preventDefault();
            if (historyCursor === -1) return;
            historyCursor = historyCursor + 1;
            if (historyCursor >= commandHistory.length) {
                historyCursor = -1;
                input.value = "";
            } else {
                input.value = commandHistory[historyCursor];
            }
            return;
        }

        if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === "l") {
            e.preventDefault();
            clearTerminal();
        }
    });

    document.addEventListener("click", () => input.focus());

    updatePrompt();
    printWelcome();
    input.focus();
})();
