const buttons = document.querySelectorAll('.notebook__section-button')
const selectDivision = document.querySelectorAll('.notebook__select-grupo-armado');
const rawButton = document.querySelector('.notebook__section-submit-raw');

const exercises = {
    one:    { url: `${baseUrl}/conflicts`, callback: refreshConflicts},
    two:    { url: `${baseUrl}/army-group`, callback: refreshOnAddArmyGroup},
    three:  { url: `${baseUrl}/army-group/division`, callback: refreshArmyGroupOnAddDivision},
    four:   { url: `${baseUrl}/political`, callback: refreshOnAddPoliticalLider},
    five:   { url: `${baseUrl}/militar`, callback: refreshMilitarLider}
}

const initNotebook = () => {
    [
        refreshArmyGroupCode,
        refreshPoliticalName,
        refreshConflicts,
        refreshMilitarGroups,
        refreshDivision,
        refreshMilitarLider,
        refreshPoliticalLider,
        refreshGunDealersAndArmyGroup,
        refreshTop5Conflicts,
        refreshTop5ONG,
        refreshTop5ArmyGroup,
        refreshTopReligousConflict
    ]
        .forEach(fn => fn.apply())
}

buttons.forEach( button => {
    const name = button.name;

    if(!!(exercises[name])){
        button.addEventListener('click', (event) => {

            const submitData = filterInput(event.target.parentNode);
            const { url, callback} = exercises[name];

            const header = new Headers();
            header.append("Content-Type", "application/json");

            fetch(url, {
                method: 'POST',
                mode: 'cors',
                headers: header,
                body: JSON.stringify(submitData)
            })
                .then(callback)
        })
    }
})

selectDivision.forEach(select => select.addEventListener('change',  handleDivisionChange))
rawButton.addEventListener('click', (event) => submitRawQuery(event.target))
window.addEventListener('load', initNotebook);

