import http from 'k6/http';

export function setup(){
    const randomId = Math.random().toString(36).substring(2,15);
    const password = "12346"
    const res = http.post("http://localhost:8080/auth/register",JSON.stringify(
        {username: randomId,
            password: password,
            email:  randomId + "@example.com"}), {headers: {"Content-Type": "application/json"}});
    const jwt = res.json().token
    return jwt;
}

export default function (data){
    const httppa =  http.get("http://localhost:8080/recommendations/getRecommendations",
        { headers: { "Authorization": "Bearer " + data } })
    console.log("Recommendations took: " + httppa.timings.duration + "ms")
}