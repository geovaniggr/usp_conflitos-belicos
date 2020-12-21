const baseUrl = "http://localhost:3000/api";

const refreshPoliticalName = async () => {
    const rawPoliticalNames = await fetch(`${baseUrl}/political/names`, { method: 'GET' })

    const politicalNames = await rawPoliticalNames.json();

    const politicalNamesAsOption = dataToOption(politicalNames, political => political.nome);

    const selects = document.querySelectorAll('select[name="nomeLp"]');

    const baseOption = `<option value="">Nome do Lider Politico </option>`;

    selects.forEach( select => select.innerHTML = baseOption.concat(politicalNamesAsOption))
}

const refreshArmyGroupCode = async () => {
    const rawArmyGroupCode = await fetch(`${baseUrl}/army-group/code`, { method: 'GET' })

    const armyGroupCode = await rawArmyGroupCode.json();

    const selects = document.querySelectorAll('select[name="codigoGa"]');

    const armyGroupCodeAsOption = dataToOption(armyGroupCode, group => group.codigo)

    const baseOption = `<option value=""> Código do Grupo Armado </option>`;

    selects.forEach( select => select.innerHTML = baseOption.concat(armyGroupCodeAsOption))
}

const refreshDivisionNumberByArmyGroup = async (input, value) => {
    const rawDivisionNumber = await fetch(`${baseUrl}/army-group/division/${value}`, { method: 'GET' })

    const divisionNumber = await rawDivisionNumber.json();

    const divisionAsOption = dataToOption(divisionNumber, division => division.numero_divisao);

    const baseOption = `<option value=""> Número da Divisão </option>`;

    input.innerHTML = baseOption.concat(divisionAsOption);

}

const handleDivisionChange = (event) => {
    const armyGroup = event.target.value;

    return [...event.target.parentNode.children]
            .filter( input => input.name === 'codigoDivisao')
            .forEach( select => refreshDivisionNumberByArmyGroup(select, armyGroup));
}

const refresh = async (url, element) => {
    const rawData = await fetch(url, { method: 'GET'});
    const data = await rawData.json();

    element.innerHTML = arrayToTableRow(data);
}

const refreshConflicts = () => {
    const tr = document.querySelector('.notebook__exercise-one');
    const url = `${baseUrl}/conflicts`;
    refresh(url, tr);
}

const refreshMilitarGroups = () => {
    const tr = document.querySelector('.notebook__exercise-two');
    const url = `${baseUrl}/army-group`;
    refresh(url, tr)
}

const refreshDivision = () => {
    const tr = document.querySelector('.notebook__exercise-three');
    const url = `${baseUrl}/army-group/division`;
    refresh(url, tr)
}

const refreshPoliticalLider = () => {
    const tr = document.querySelector('.notebook__exercise-four');
    const url = `${baseUrl}/political`;
    refresh(url, tr)
}

const refreshMilitarLider = () => {
    const tr = document.querySelector('.notebook__exercise-five');
    const url = `${baseUrl}/militar`;
    refresh(url, tr)
}

const refreshGunDealersAndArmyGroup = () => {
    const url = `${baseUrl}/report/top/dealers`;
    const tr = document.querySelector('.notebook__exercise-seven');
    refresh(url, tr);
}

const refreshTop5Conflicts = () => {
    const url = `${baseUrl}/report/top/number-of-death`;
    const tr = document.querySelector('.notebook__exercise-eight');
    refresh(url, tr);
}

const refreshTop5ONG = () => {
    const url = `${baseUrl}/report/top/ongs`;
    const tr = document.querySelector('.notebook__exercise-nine');
    refresh(url, tr);
}

const refreshTop5ArmyGroup = () => {
    const url = `${baseUrl}/report/top/army-group`;
    const tr = document.querySelector('.notebook__exercise-ten');
    refresh(url, tr);
}

const refreshTopReligousConflict = () => {
    const url = `${baseUrl}/report/top/religious-country`;
    const tr = document.querySelector('.notebook__exercise-eleven');
    refresh(url, tr);
}

const refreshArmyGroupOnAddDivision = () => {
    refreshDivision();
    refreshMilitarGroups();
}

const refreshOnAddArmyGroup = () => {
    refreshMilitarGroups();
    refreshArmyGroupCode();
}

const refreshOnAddPoliticalLider = () => {
    refreshPoliticalLider();
    refreshPoliticalName()
}

const submitRawQuery = (button) => {
    const parentElement = button.parentNode;

    const textArea = parentElement.querySelector('textarea');
    const table = parentElement.querySelector('table')

    const sql = textArea.value.replaceAll("\n", " ");

    const header = new Headers();
    header.append("Content-Type", "application/json");

    fetch(`${baseUrl}/raw`, {
        method: 'POST',
        mode: 'cors',
        headers: header,
        body: JSON.stringify({ sql })
    })
        .then(response => response.json())
        .then( data => table.innerHTML = JsonToTable(data))
}
