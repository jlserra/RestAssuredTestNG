package initialTopics;

import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import payload.Payload;

public class ComplexJsonParse {

    public static void main(String[] args) {

        JsonPath jsonPath = new JsonPath(Payload.coursePrice());
        int purchaseAmount = jsonPath.getInt("dashboard.purchaseAmount");
        int courseSize = jsonPath.getInt("courses.size()");
        int totalComputedPurchaseAmount = 0;
        for (int i = 0; i < courseSize; i++) {
            System.out.println("course: " + jsonPath.getString("courses[" + i + "].title"));
            int courseTotalAmount = jsonPath.getInt("courses[" + i + "].price") * jsonPath.getInt("courses[" + i + "].copies");
            System.out.println("courseTotalAmount: " + courseTotalAmount);
            totalComputedPurchaseAmount += courseTotalAmount;
        }

        System.out.println("purchaseAmount: " + purchaseAmount);
        System.out.println("totalComputedPurchaseAmount: " + totalComputedPurchaseAmount);

        Assert.assertEquals(purchaseAmount,totalComputedPurchaseAmount);

    }

}
