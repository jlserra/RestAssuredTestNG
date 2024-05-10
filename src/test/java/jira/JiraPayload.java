package jira;

public class JiraPayload {

    public static String jiraLogin(String username, String password) {
        return "{\n" +
                "    \"username\": \""+username+"\",\n" +
                "    \"password\": \""+password+"\"\n" +
                "}";
    }

    public static String jiraCreateIssues(String key, String summary, String description, String issuetype){
        return "{\n" +
                "    \"fields\": {\n" +
                "        \"project\":{\n" +
                "            \"key\":\""+key+"\"\n" +
                "        },\n" +
                "        \"summary\":\""+summary+"\",\n" +
                "        \"description\":\""+description+"\",\n" +
                "        \"issuetype\":{\n" +
                "            \"name\":\""+issuetype+"\"\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }

    public static String jiraCommentIssue(String body){
        return "{\n" +
                "    \"body\": \""+body+"\",\n" +
                "    \"visibility\": {\n" +
                "        \"type\": \"role\",\n" +
                "        \"value\": \"Administrators\"\n" +
                "    }\n" +
                "}";
    }


}
