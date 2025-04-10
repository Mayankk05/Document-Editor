// Connect to WebSocket server
const socket = new WebSocket("ws://localhost:8080/ws");

const editor = document.getElementById("editor");
const saveBtn = document.getElementById("save-btn");

// Receive real-time updates from WebSocket
socket.onmessage = (event) => {
    const data = JSON.parse(event.data);
    editor.value = data.content;
};

// Detect changes in the editor and send updates
editor.addEventListener("input", () => {
    const content = editor.value;
    socket.send(JSON.stringify({ id: "doc1", content: content }));
});

// Save button functionality
saveBtn.addEventListener("click", () => {
    fetch("http://localhost:8080/document/save", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ id: "doc1", content: editor.value }),
    })
    .then(response => response.json())
    .then(data => alert("Document saved!"))
    .catch(error => console.error("Error saving document:", error));
});
