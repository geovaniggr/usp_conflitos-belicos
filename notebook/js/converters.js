const toSnakeCase = (pascalCase) => pascalCase.replace(/([A-Z])/g, "_$1").toLowerCase();

const csvToArray = (stringAsCsv) => stringAsCsv
                                    .split(",")
                                    .filter( element => !!element)
                                    .map( element => element.trim())

const filterInput = (parentElement) => {
    return [...parentElement.children]
        .filter( element => element.type !== 'button' && !!element.name)
        .reduce( (obj, current) => {
            const snakeCasedName = toSnakeCase(current.name);

            const value = current.value.includes(",")
                ? csvToArray(current.value)
                : current.value;

            return {...obj, [snakeCasedName]: value }
        }, {} )

}

const objectToTableData = (obj) => Object
                                    .values(obj)
                                    .map( value => `<td> ${value} </td>`)
                                    .join("\n");

const arrayToTableRow = (array) => array
                                    .map(objectToTableData)
                                    .reduce( (tr, current) => tr.concat(`<tr> ${current} </tr>`), "")

const dataToOption = (data, fn) => data
                                    .map(fn)
                                    .map( element => `<option value="${element}">${element} </option>`)
                                    .reduce( (opt, current) => opt.concat(current), "")


const JsonToTable = (data) => {

    const first = data[0];

    const th = Object
        .keys(first)
        .map( element => `<th> ${element} </th>`)
        .join("");

    const td = data
        .map(objectToTableData)
        .map( value => `<tr> ${value} </tr>`)
        .join("")

    const table = `
        <thead>
            <tr>
                ${th}
            </tr>
        </thead>
        <tbody class="notebook__exercise-eight">
                <tr>
                    ${td}
                </tr>
        </tbody>
    `;

    return table;
}
