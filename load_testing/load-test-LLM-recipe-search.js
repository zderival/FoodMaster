import http from 'k6/http';

export const options = {
    vus: 3,
    duration: '30s'
}

export function setup() {
    const randomId = Math.random().toString(36).substring(2, 15);
    const password = "12346"

    const res = http.post("http://localhost:8080/auth/register",
        JSON.stringify(
            {
                username: randomId,
                password: password,
                email: randomId + "@example.com"
            }),
        {headers: {"Content-Type": "application/json"}});

    const jwt = res.json().token
    return jwt;

}

export default function (data) {
    const res = http.post("http://localhost:8080/recipes/search",
        JSON.stringify(
            {ingredients: ["motor oil", "trees"]}),
        {headers: {"Content-Type": "application/json", "Authorization": "Bearer " + data }} )
    console.log("LLM took: " + res.timings.duration + "ms")
    console.log("Search status: " + res.status)

}
