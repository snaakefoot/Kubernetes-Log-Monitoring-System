import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.handler.api.MyJsonProtocol
import com.handler.model.Myrequest
import com.handler.api.MyRoutes
import com.handler.api.MyJsonProtocol
import com.handler.services.loginService.isTokenValid
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
class LoginSpec extends AnyWordSpecLike with Matchers with ScalatestRouteTest with MyJsonProtocol {

  "Register Function" should {
    "successful Register" in {
      val loginRequest=Myrequest("1234@123","123")
      Post("/signin",loginRequest)~>MyRoutes.ServerRoutes~> check {
        status shouldBe StatusCodes.OK
      }
    }
    "failed Register" in {
      val loginRequest=Myrequest("1234@123","123")
      Post("/signin",loginRequest)~>MyRoutes.ServerRoutes~> check {
        status shouldBe StatusCodes.Conflict
      }
    }
  }
  "Login Function" should {
    "succssful login" in {
      val loginRequest=Myrequest("123@123","123")
      Post("/signin",loginRequest)~>MyRoutes.ServerRoutes
      Post("/login",loginRequest)~>MyRoutes.ServerRoutes~> check {
        status shouldBe StatusCodes.OK
        isTokenValid(responseAs[String]) shouldBe true
      }
    }
    "failed login" in {
      val loginRequest=Myrequest("123@123","1234")
      Post("/login",loginRequest)~>MyRoutes.ServerRoutes~> check {
        status shouldBe StatusCodes.Unauthorized
      }
    }
  }
}