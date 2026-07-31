import http from 'k6/http';

export function setup(){
    const randomId = Math.random().toString(36).substring(2,15);
    const password = "12346"

    const res = http.post("http://localhost:8080/auth/register",
        JSON.stringify(
            { username: randomId,
                password: password,
                email:  randomId + "@example.com" }),
        {headers: {"Content-Type": "application/json"}});

    const jwt = res.json().token

    const profile = http.post("http://localhost:8080/profile/create",JSON.stringify(
            { goal: "lean",
                allergies: [],
                preferences: [],
                diet: ""
            }
        ),
        { headers:
                { "Content-Type": "application/json", "Authorization": "Bearer " + jwt }
        }
    )

    const save_recipe = http.post("http://localhost:8080/saved/save",JSON.stringify(
            {spoonacularId: 648320}
        ), {headers: {"Content-Type": "application/json", "Authorization": "Bearer " + jwt }}
    )
    console.log("Save status: " + save_recipe.status)
    console.log("Save error: " + save_recipe.error)
    return jwt;
}

export default function (data){
    const httppa =  http.get("http://localhost:8080/recommendations/getRecommendations",
        { headers: { "Authorization": "Bearer " + data } })
    console.log("Recommendations took: " + httppa.timings.duration + "ms")
}