package com.manomano.workshop.web;

import com.manomano.workshop.RaceCondition;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.css.Counter;
import org.xml.sax.SAXException;


@RestController
@RequestMapping("/api")
public class Api {

    public static HashMap<Integer, Object> cards;

    @Autowired
    public void setUpFakeFb() {
        // Please consider this as a "real database" :)
        cards = new HashMap<>();
        HashMap<String, Object> card = new HashMap<>();
        card.put("owner_id", 1);
        card.put("card_info", "VISA, 4150321684570625, 8/2025, 425");
        cards.put(1, card);

        card = new HashMap<>();
        card.put("owner_id", 1);
        card.put("card_info", "VISA, 4638954867020927, 10/2024, 704");
        cards.put(2, card);

        card = new HashMap<>();
        card.put("owner_id", 1);
        card.put("card_info", "VISA, 4024007142998490, 10/2028, 143");
        cards.put(3, card);

        card = new HashMap<>();
        card.put("owner_id", 2);
        card.put("card_info", "VISA, 4669594232416137, 4/2024, 639");
        cards.put(4, card);

        card = new HashMap<>();
        card.put("owner_id", 3);
        card.put("card_info", "VISA, 4024007184111184, 10/2025, 718");
        cards.put(5, card);

        card = new HashMap<>();
        card.put("owner_id", 4);
        card.put("card_info", "VISA, 4716689026097623, 12/2028, 356");
        cards.put(6, card);

        card = new HashMap<>();
        card.put("owner_id", 5);
        card.put("card_info", "VISA, 4556880732449439, 8/2028, 263");
        cards.put(7, card);

        card = new HashMap<>();
        card.put("owner_id", 6);
        card.put("card_info", "VISA, 4024007166273564, 9/2023, 807");
        cards.put(8, card);

        card = new HashMap<>();
        card.put("owner_id", 6);
        card.put("card_info", "VISA, 4619909841583211, 6/2022, 948");
        cards.put(9, card);

        //HashMap<String,Object> map2 = (HashMap<String,Object>)map.get("myMap");
        //System.out.println(map2.get("int"));
    }

    @GetMapping(path = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> sayHello() {
        HashMap<String, String> map = new HashMap<>();
        map.put("foo", "bar");
        return map;
    }

    // IDOR
    @GetMapping(path = "/user/{user}/billing/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> getBillingInfo(@PathVariable("user") int user_id,
        @PathVariable("id") int card_id) {
        // Assuming user parameter is trusted, user is already logged-in
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", String.valueOf(user_id));
        map.put("card_id", String.valueOf(card_id));
        @SuppressWarnings("unchecked")
        HashMap<String, Object> card = (HashMap<String, Object>) this.cards.get(card_id);
        map.put("card_info", (String) card.get("card_info"));
        map.put("card_owner", card.get("owner_id").toString());
        return map;
    }

    // SSRF
    @GetMapping(path = "/user/fetchProfile", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> fetchProfile(@RequestParam("url") String url) throws IOException {
        String content = "empty";
        URL u = new URL(url);
        InputStream in = u.openStream();
        content = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        HashMap<String, String> map = new HashMap<>();
        map.put("content", content);

        return map;
    }

    @GetMapping(path = "/protected-feature", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> protectedFeature(HttpServletRequest request,
        HttpServletResponse response) throws IOException, ClassNotFoundException {
        System.out.println(request.getRemoteAddr());
        if (!request.getRemoteAddr().equals("127.0.0.1")) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            HashMap<String, String> map = new HashMap<>();
            map.put("content", "Denied, bad source IP");
            return map;
        }
        return System.getenv();
    }


    @GetMapping(path = "/debug-feature", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> debugFeature(@RequestParam("serializedObject") String serializedObject)
        throws IOException, ClassNotFoundException {
        System.out.println(serializedObject);
        byte[] base64decodedBytes = Base64.getDecoder().decode(serializedObject);
        InputStream in = new ByteArrayInputStream(base64decodedBytes);
        ObjectInputStream obin = new ObjectInputStream(in);
        System.out.println("Will read object");
        Object object = obin.readObject();
        System.out.println("Object read, Will call toString()");
        System.out.println("Deserialised the object: \n" + object.toString());
        System.out.println("toString called, will output json");
        HashMap<String, String> map = new HashMap<>();
        map.put("content", "ok");
        return map;
    }

    @PostMapping(path = "/xml", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> xmlParser(@RequestBody String xmlBody)
        throws ParserConfigurationException, IOException, SAXException, TransformerException {
        System.out.println(xmlBody);
        InputStream xmlIs = new ByteArrayInputStream(xmlBody.getBytes());
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(xmlIs);
        doc.getDocumentElement().normalize();
        System.out.println("Root Element :" + doc.getDocumentElement().getNodeName());
        HashMap<String, String> map = new HashMap<>();

        StringWriter sw = new StringWriter();
        Transformer t = null;
        t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.METHOD, "xml");
        t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        t.transform(new DOMSource(doc), new StreamResult(sw));

        map.put("content", sw.toString());
        return map;
    }

    @GetMapping("/path-traversal")
    public List<String> pathTraversal(@RequestParam String dir) throws IOException {

        if(!dir.contains("img")) {
            return null;
        }
        List<String> files = new ArrayList<>();
        String normalizedPath = Paths.get(dir).normalize().toString();
        File file = new File(normalizedPath);

        if (file.isDirectory()) {
            for (File f : Objects.requireNonNull(file.listFiles())) {
                files.add(f.getAbsolutePath());
            }
            return files;
        }
        return Collections.singletonList(new String(Files.readAllBytes(file.toPath())));
    }

    @GetMapping("/command-injection-ls")
    public List<String> commandInjection(@RequestParam String path) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", "ls " + path);

        List<String> returnCommand = new ArrayList<>();
        try {

            Process process = processBuilder.start();
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                returnCommand.add(line);
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                return returnCommand;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return returnCommand;
    }

    @GetMapping("/sql-injection")
    public String sqlInjection(@RequestParam String username, @RequestParam String password)
        throws SQLException {
        String sqlQuery =
            "SELECT * FROM users u where u.username='" + username + "' AND u.password='" + password + "'";
        String message = "Forbidden";
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/workshops", "userdefault",
                "password");
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            if (rs.next()) {
                if ("user".equals(rs.getString("username"))) {
                    message = "Welcome back user !";
                }
                if ("admin".equals(rs.getString("username"))) {
                    message = "Welcome back admin !";
                    // ';--%20
                }
            }
            if(stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  message;
    }

    @GetMapping("/coupons")
    public List<String> getCoupons(@RequestParam String username) {

        String sqlQuery = "SELECT code_coupon FROM coupon c where c.username=?";

        List<String> listCoupons = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/workshops", "userdefault",
                "password");
            stmt = conn.prepareStatement(sqlQuery);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                listCoupons.add(rs.getString("code_coupon"));
            }
            if(stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listCoupons;
    }
    @GetMapping("/race-condition")
    public List<String> raceCondition(@RequestParam String username)
        throws SQLException, InterruptedException {
        List<String> listCoupons = this.getCoupons(username);
        RaceCondition rc = new RaceCondition();
        Thread t1 = new Thread(RaceCondition.getRunnable(rc, listCoupons.size(), username),
            "Thread-1");
        t1.sleep(1000); // This simulate network latency, do not remove it thanks :)

        t1.start();

        return this.getCoupons(username);
    }


}
