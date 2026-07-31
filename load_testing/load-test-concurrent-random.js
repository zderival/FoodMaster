import http from 'k6/http';

export const options = {
    vus: 20,
    duration: '10s'
}

export function setup(){
    const res = http.post("http://localhost:8080/recipes/search",
        JSON.stringify({ ingredients: ["chicken", "rice"] }),
        { headers: { "Content-Type": "application/json" } }
    )
    const recipes = res.json();
    const ids = recipes.map(recipe => recipe.spoonacularId);
    return ids;
}

export default function (data) {
    const randomIndex = Math.floor(Math.random() * data.length);
    const randomId = data[randomIndex];
    http.get(`http://localhost:8080/recipes/${randomId}`)
}