function showTaskForm() {
    const task = document.getElementById("task-select").value;
    const formContainer = document.getElementById("input-form");
    formContainer.innerHTML = ''; // Clear previous form

    switch (task) {
        case "createRule":
            formContainer.innerHTML = `
                <h2>Create Rule</h2>
                <label for="rule-string">Rule String:</label>
                <input type="text" id="rule-string" placeholder="e.g., age > 30 AND department = 'HR'">
                <button onclick="createRule()">Submit</button>
            `;
            break;

        case "combineRules":
            formContainer.innerHTML = `
                <h2>Combine Rules</h2>
                <label for="rule-1">Rule 1:</label>
                <input type="text" id="rule-1" placeholder="e.g., age > 30 AND department = 'Sales'">
                <label for="rule-2">Rule 2:</label>
                <input type="text" id="rule-2" placeholder="e.g., salary > 50000">
                <button onclick="combineRules()">Submit</button>
            `;
            break;

        case "evaluateRule":
            formContainer.innerHTML = `
                <h2>Evaluate Rule</h2>
                <label for="json-data">JSON Data:</label>
                <textarea id="json-data" placeholder='e.g., {"age": 35, "department": "HR", "salary": 60000}'></textarea>
                <button onclick="evaluateRule()">Submit</button>
            `;
            break;

        case "evaluateUser":
            formContainer.innerHTML = `
                <h2>Evaluate Users Table</h2>
                <label for="ast-rule">AST Rule:</label>
                <input type="text" id="ast-rule" placeholder="e.g., AST representation here">
                <button onclick="evaluateUsersTable()">Submit</button>
            `;
            break;
    }
}

function createRule() {
    const ruleString = document.getElementById("rule-string").value;
    fetch('/rules/create', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ ruleString })
    })
    .then(response => response.json())
    .then(data => document.getElementById("output-content").textContent = JSON.stringify(data, null, 2))
    .catch(error => document.getElementById("output-content").textContent = error);
}

function combineRules() {
    const rule1 = document.getElementById("rule-1").value;
    const rule2 = document.getElementById("rule-2").value;
    fetch('/rules/combine', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify([rule1, rule2])
    })
    .then(response => response.json())
    .then(data => document.getElementById("output-content").textContent = JSON.stringify(data, null, 2))
    .catch(error => document.getElementById("output-content").textContent = error);
}

function evaluateRule() {
    const jsonData = document.getElementById("json-data").value;
    fetch('/rules/evaluate', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: jsonData
    })
    .then(response => response.json())
    .then(data => document.getElementById("output-content").textContent = JSON.stringify(data, null, 2))
    .catch(error => document.getElementById("output-content").textContent = error);
}

function evaluateUsersTable() {
    const astRule = document.getElementById("ast-rule").value;
    fetch('/rules/evaluate-all', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(astRule) // Convert to JSON format for POST request
    })
    .then(response => response.json())
    .then(data => document.getElementById("output-content").textContent = JSON.stringify(data, null, 2))
    .catch(error => document.getElementById("output-content").textContent = error);
}
