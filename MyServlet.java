package MyPackage;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class MyServlet
 */
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		String userinputData = request.getParameter("userinput");
		String apiKey = "bb95e29dbedc4d929be90b0dd99954e0";
		String city = request.getParameter("city");
		String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;
		
		try {
			URL url = new URL(apiUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			
			InputStream inputStream = connection.getInputStream();
			InputStreamReader Reader = new InputStreamReader(inputStream);
			
			StringBuilder str = new StringBuilder();
			Scanner scn = new Scanner(Reader);
			
			while(scn.hasNext()) {
				str.append(scn.nextLine());
			}
	//		System.out.println(str);
			
			scn.close();
			
	//		Typecasting = Parsing the data into JSON from string
			Gson gson = new Gson();
			JsonObject jsonObject = gson.fromJson(str.toString() , JsonObject.class);
	//		
			long dateTime = jsonObject.get("dt").getAsLong() * 1000;
			String date = new Date(dateTime).toString();
	
			JsonObject main = jsonObject.getAsJsonObject("main");
	        double temperature = main.has("temp") ? main.get("temp").getAsDouble() : 0.0;
	        int temp = (int) (temperature - 273.15);
			
	        int humidity = main.has("humidity") ? main.get("humidity").getAsInt() : 0;
	
			
	        JsonObject wind = jsonObject.getAsJsonObject("wind");
	        double windSpeed = wind.has("speed") ? wind.get("speed").getAsDouble() : 0.0;
			
			String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();
	//		System.out.println(date);
			
			request.setAttribute("date" , date);
			request.setAttribute("city" , city);
			request.setAttribute("humidity", humidity);
			request.setAttribute("windSpeed" , windSpeed);
			request.setAttribute("temperature" , temp);
			
			connection.disconnect();
		} catch(IOException e) {
			e.printStackTrace();		}
		
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		rd.forward(request, response);
	}

}
