package com.handler
import io.circe.Json
import io.circe._
import io.circe.syntax._
import io.circe.parser._

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import scala.io.StdIn
import akka.stream.scaladsl.Flow
import akka.http.scaladsl.model.ws.{ Message, TextMessage }
import java.time._
import java.time.format._
import com.handler.model.MyEntity
import scala.util.parsing.json.JSONObject
import scala.util.Try
object

playground {
    val ch = "2024-03-27 11:00:47.751 INFO  com.cognira.shared.metric.models.MetricsEntity - Logs for FluentBit are : Map(sales_units_lift_percentage_sf -> None, batch_forecasted -> None, promotional_sales_margin_dollars_if -> 143456.33, fiscal_period -> List(202409), sales_margin_dollars_lift_td -> None, location_group_ids -> List(All_Stores_CUB), base_aur_af -> None, base_auc_if -> 340.23352, created_by -> Promo Planner, assignee_id -> 2, promotional_sales_dollars_sf -> None, category_managers -> List(), base_sales_units_if -> 306.8038, forecast_gross_margin_percent -> None, sales_margin_dollars_lift_uf -> -12272.145, base_sales_units_td -> None, tenant_id -> 0, promo_approver -> None, do_not_send_pricing_to_ea -> false, total_sales_units_sf -> None, regular_sales_dollars_af -> None, sub_category -> List(REFRIGERATOR), base_sales_dollars_af -> None, triggered_computation -> List(isolated_forecast, user_forecast, promo_aur_uf_from_base_aur_uf), total_sales_dollars_af -> None, promotional_sales_units_af -> None, base_sales_dollars_uf -> 260113.4, base_sales_margin_dollars_uf -> 155728.47, created_by_id -> 2, base_cost_dollars_min_uf -> 159.98999, total_sales_dollars_af_rel_diff_to_td -> None, custom_headers -> Map(do_not_send_pricing_to_ea -> false, vehicle -> Event Only, advertising_instructions -> , bold_ad_text -> aloo, product_group -> null, advertising_bug -> null, internal_comments -> , card_type -> No Card), primary_image_id -> None, base_sales_margin_dollars_td -> None, adjusted_cost_dollars -> 399.99, base_aur_uf -> None, sales_units_lift_td -> None, regular_sales_margin_dollars_if -> 0, promo_description -> None, size_description -> None, vendor_unit_contribution_dollars_uf -> None, permission -> rfvoesd, lift_vs_spend_percentage_uf -> -0.9999994, sales_units_lift_percentage_if -> 0, sales_dollars_lift_uf -> -12272.149, base_sales_units_sf -> None, vehicle -> Event Only, base_sales_margin_dollars_af -> None, cost_dollars -> 399.99, sub_category_id -> List(9112), average_unit_retail -> None, total_sales_units_if -> 306.8038, regular_price_dollars_max_uf -> 954.29, cloned_from -> None, specific_fields -> Map(primary_image_id -> null, vendor_unit_contribution_dollars_uf -> null, secondary_image_id -> null, vendor_unit_contribution_percentage_uf -> null, promo_expression -> GET $50.00 off firas), is_versionable -> false, versionability_details -> Insufficient Location groups to version the promotion , max_price -> None, promo_start_date -> 2024-04-02, sales_dollars_lift_percentage_af -> None, regular_sales_units_td -> None, max_cost -> None, promotional_sales_margin_dollars_td -> None, forecast_comment_last_update -> None, promo_type -> Amount Off, base_aur_if -> 847.8168, assignee_name -> Promo Planner, regular_sales_margin_dollars_uf -> 0, batch_processed -> false, internal_comment_enabled -> true, promo_auc_sf -> None, base_auc_af -> None, min_cost -> None, split_id -> None, sales_dollars_lift_percentage_if -> -0.047179997, total_sales_margin_dollars_uf -> 143456.33, sales_margin_dollars_lift_percentage_sf -> None, regular_sales_units_if -> 0, total_sales_dollars_sf_rel_diff_to_td -> None, total_sales_units_td_rel_diff_to_af -> None, total_sales_units_uf -> None, value_to_customer_percentage -> None, base_sales_units_af -> None, total_sales_margin_dollars_if -> 143456.33, sales_dollars_lift_if -> -12272.149, base_sales_dollars_td -> None, commentator_user_id -> 2, promo_group_id -> None, total_sales_margin_percentage_if -> 0.5788234, promo_aur_sf -> None, promotional_sales_margin_dollars_sf -> None, promotional_margin_percentage -> None, department_id -> List(14), computing -> true, total_sales_dollars_td -> None, large_promo -> false, base_aur_sf -> None, promo_batch_computed -> None, regular_sales_margin_dollars_af -> None, versions_ids -> List(), nullify_overridable -> None, total_sales_units_af_rel_diff_to_td -> None, total_sales_margin_percentage_uf -> 0.5788234, regular_sales_margin_dollars_sf -> None, created_from_event_id -> None, lift_vs_spend_percentage_td -> None, lift_vs_spend_percentage_af -> None, drift_changed -> None, sales_units_lift_percentage_td -> None, product_count -> 23, brand -> List(FRIGIDAIRE, WHIRLPOOL, SAMSUNG, LG, BOSCH), base_auc_sf -> None, created_from_invitation_id -> None, sales_units_lift_sf -> None, promo_status -> Ready, sales_margin_dollars_lift_af -> None, advertising_instructions -> , promotional_sales_units_td -> None, redemption_count_td -> None, base_sales_margin_dollars_if -> 155728.47, event_selections_count -> 0, sub_department_id -> List(), approval_date -> None, submission_status -> None, promotional_sales_dollars_af -> None, total_sales_units_td_rel_diff_to_sf -> None, total_sales_units_af -> None, promotional_sales_margin_dollars_uf -> 143456.33, base_auc_uf -> None, department -> List(APPLIANCES), sales_dollars_lift_af -> None, total_sales_units_td -> None, sales_units_lift_percentage_uf -> 0, secondary_image_id -> None, inherit_financial_conditions -> true, promo_name -> testX, promo_auc_uf_override -> None, regular_sales_units_uf -> 0, fiscal_week -> List(2024-03-31), sub_department -> List(), sales_dollars_lift_sf -> None, regular_sales_dollars_sf -> None, promotional_sales_units_uf -> 306.8038, fiscal_quarter -> List(202403), lift_vs_spend_percentage_if -> -0.9999994, promotional_sales_units_sf -> None, sales_dollars_lift_percentage_uf -> -0.047179997, regular_sales_dollars_td -> None, units_per_redemption_td -> None, finalized_event_selection_count -> None, base_cost_dollars_max_uf -> 559.99, promo_aur_af -> None, base_sales_margin_dollars_sf -> None, has_comments -> false, total_sales_margin_percentage_af -> None, vendor_unit_contribution_percentage_uf -> None, discount_percentage -> None, promo_aur_if -> 807.81683, internal_contribution -> None, promotional_sales_discount_dollars_if -> None, promo_id -> P00000009, used_in -> None, forecast_gross_margin_dollars -> None, total_sales_margin_dollars_sf_rel_diff_td -> None, sales_units_lift_uf -> 0, sales_margin_dollars_lift_sf -> None, total_sales_margin_dollars_td -> None, product_group_ids -> List(86b224c3-1bd6-44fd-87e8-99c016018ed3), total_sales_dollars_sf -> None, sales_units_lift_if -> 0, primary_objective -> None, promotional_margin_dollars -> None, sales_margin_dollars_lift_percentage_uf -> -0.07880476, total_sales_dollars_if -> 247841.27, total_sales_dollars_uf -> 247841.27, event_selections -> List(), overridden_metrics -> List(), sales_units_lift_af -> None, content -> , sales_dollars_lift_td -> None, total_sales_units_sf_rel_diff_to_td -> None, media -> None, sales_margin_dollars_lift_percentage_af -> None, promo_auc_uf -> None, promotional_sales_dollars_uf -> 247841.27, sales_margin_dollars_lift_percentage_if -> -0.07880476, category -> List(MAJOR KITCHEN APPLIANCE), location_count -> 125, bold_ad_text -> aloo, promo_auc_af -> None, total_sales_margin_percentage_td -> None, average_unit_cost -> None, sales_dollars_lift_percentage_td -> None, category_id -> List(432), product_group -> None, created_at -> 1711960123, submitted_by -> None, promo_auc_if -> 340.23352, required_fields -> true, finalized_event_selection_ids -> List(), discount_dollars -> 50, advertising_bug -> None, sales_dollars_lift_percentage_sf -> None, total_sales_margin_dollars_sf -> None, auc_changed -> None, regular_sales_units_af -> None, promotional_sales_dollars_td -> None, regular_price_dollars -> 899.99, base_sales_dollars_sf -> None, regular_sales_dollars_uf -> 0, promotional_sales_units_if -> 306.8038, is_reopened -> Yes, sales_margin_dollars_lift_if -> -12272.145, vendor_funding -> None, forecast_sales_dollars -> None, finalized_event_ids -> List(), regular_sales_units_sf -> None, total_sales_margin_dollars_af -> None, regular_price_dollars_min_uf -> 599.99, promo_expression -> GET $50.00 off firas, lift_vs_spend_percentage_sf -> None, promotional_sales_margin_dollars_af -> None, updated_at -> 1711962557, category_manager -> None, regular_sales_dollars_if -> 0, has_versions -> false, rejected_by -> None, value_to_customer_dollars -> None, base_sales_dollars_if -> 260113.4, promotional_price_dollars -> 849.99, promo_end_date -> 2024-04-03, approved_by -> None, event_submission_count -> 0, redemption_rate_td -> None, sales_margin_dollars_lift_percentage_td -> None, total_sales_margin_dollars_af_rel_diff_to_td -> None, base_sales_units_uf -> 306.8038, internal_comments -> , vendor_promo_expense_percentage -> None, aur_changed -> None, internal_promo_expense_percentage -> None, promo_aur_uf -> None, parity_id -> None, promo_group_name -> None, event_submission_ids -> List(), card_type -> No Card, regular_sales_margin_dollars_td -> None, min_price -> None, total_sales_margin_percentage_sf -> None, promotional_sales_dollars_if -> 247841.27, forecast_sales_units -> None, created_or_mutated_when_versioning_at_finalization -> None, sales_units_lift_percentage_af -> None, commentator_user_name -> Promo Planner)"

    val dataMap = Map(
        "fiscal_period" -> List(202408, 202409, 202410),
        "location_group_ids" -> List("All_Stores_CUB"),
        "created_by" -> "Promo Planner",
        "selectedImages" -> List(),
        "assignee_id" -> 2,
        "category_managers" -> List(),
        "tenant_id" -> 0,
        "do_not_send_pricing_to_ea" -> false,
        "sub_category" -> List("REFRIGERATOR"),
        "triggered_computation" -> List("isolated_forecast", "user_forecast", "promo_aur_uf_from_base_aur_uf"),
        "created_by_id" -> 2,
        "base_cost_dollars_min_uf" -> None,
        "custom_headers" -> Map(
            "do_not_send_pricing_to_ea" -> false,
            "vehicle" -> "Event Only",
            "advertising_instructions" -> "",
            "bold_ad_text" -> "",
            "product_group" -> null,
            "advertising_bug" -> null,
            "internal_comments" -> "",
            "card_type" -> "No Card"
        ),
        "primary_image_id" -> None,
        "adjusted_cost_dollars" -> 399.99,
        "vendor_unit_contribution_dollars_uf" -> 0,
        "is_secondary_image_overridden" -> false,
        "vehicle" -> "Event Only",
        "locationGroupsStores" -> List(),
        "cost_dollars" -> 399.99,
        "sub_category_id" -> List(9112),
        "regular_price_dollars_max_uf" -> None,
        "cloned_from" -> None,
        "specific_fields" -> Map(
            "vendor_unit_contribution_dollars_uf" -> 0,
            "is_secondary_image_overridden" -> false,
            "is_primary_image_overridden" -> false,
            "vendor_unit_contribution_percentage_uf" -> 0,
            "promo_expression" -> "GET $69.00 off firas"
        ),
        "promo_start_date" -> "2024-03-28",
        "promo_type" -> "Amount Off",
        "assignee_name" -> "Promo Planner",
        "is_primary_image_overridden" -> false,
        "commentator_user_id" -> 2,
        "department_id" -> List(14),
        "computing" -> true,
        "large_promo" -> false,
        "created_from_event_id" -> None,
        "locationGroups" -> List(),
        "product_count" -> 23,
        "brand" -> List("FRIGIDAIRE", "WHIRLPOOL", "SAMSUNG", "LG", "BOSCH"),
        "created_from_invitation_id" -> None,
        "promo_status" -> "Ready",
        "advertising_instructions" -> "",
        "sub_department_id" -> List(),
        "department" -> List("APPLIANCES"),
        "secondary_image_id" -> None,
        "inherit_financial_conditions" -> true,
        "promo_name" -> "testx",
        "fiscal_week" -> List("2024-03-24", "2024-03-31", "2024-04-07", "2024-04-14", "2024-04-21", "2024-04-28", "2024-05-05"),
        "sub_department" -> List(),
        "fiscal_quarter" -> List(202403, 202404),
        "base_cost_dollars_max_uf" -> None,
        "has_comments" -> false,
        "vendor_unit_contribution_percentage_uf" -> 0,
        "version_name" -> "",
        "promo_id" -> "P00000006",
        "product_group_ids" -> List("86b224c3-1bd6-44fd-87e8-99c016018ed3"),
        "selectedLocationGroups" -> List(),
        "content" -> "",
        "category" -> List("MAJOR KITCHEN APPLIANCE"),
        "location_count" -> 125,
        "bold_ad_text" -> "",
        "promotionType" -> Map(),
        "productGroups" -> List(),
        "category_id" -> List(432),
        "product_group" -> None,
        "created_at" -> 1711537247,
        "required_fields" -> true,
        "discount_dollars" -> 69,
        "advertising_bug" -> None,
        "regular_price_dollars" -> 899.99,
        "was_forecasted" -> false,
        "is_reopened" -> "No",
        "regular_price_dollars_min_uf" -> None,
        "promo_expression" -> "GET $69.00 off firas",
        "updated_at" -> 1711537247,
        "promotional_price_dollars" -> 830.99,
        "promo_end_date" -> "2024-05-10",
        "internal_comments" -> "",
        "card_type" -> "No Card",
        "commentator_user_name" -> "Promo Planner"
    )

    implicit val encodeAny: Encoder[Any] = new Encoder[Any] {
        final def apply(a: Any): Json = a match {
            case n: Int => Json.fromInt(n)
            case s: String => Json.fromString(s)
            case b: Boolean => Json.fromBoolean(b)
            case l: List[_] => Json.fromValues(l.map(apply))
            case m: Map[_, _] if m.keys.forall(_.isInstanceOf[String]) =>
                Json.fromFields(m.asInstanceOf[Map[String, Any]].mapValues(apply))
            // Add more cases as necessary for other types you expect
            case _ => Json.Null // or throw an exception if you encounter an unexpected type
        }
    }

    val json = dataMap.asJson
    val jsonString1 = """{"@timestamp":1712143157.638594,"stream":"stdout","logtag":"F","message":"2024-04-03 11:19:17.638 INFO  com.cognira.shared.metric.models.MetricsEntity - Logs for FluentBit are : {\"id\":\"1userpromo\",\"p1\":\"1\",\"c1\":\"P00000011\",\"permission\":\"tests\",\"batch_forecasted\":null,\"promotional_sales_margin_dollars_if\":null,\"fiscal_period\":[\"202409\"],\"sales_margin_dollars_lift_td\":null,\"location_group_ids\":[\"All_Stores_CUB\"],\"base_aur_af\":null,\"base_auc_if\":null,\"created_by\":\"Promo Planner\",\"assignee_id\":\"2\",\"promotional_sales_dollars_sf\":null,\"category_managers\":[],\"base_sales_units_if\":null,\"forecast_gross_margin_percent\":null,\"sales_margin_dollars_lift_uf\":null,\"base_sales_units_td\":null,\"promo_approver\":null,\"do_not_send_pricing_to_ea\":true,\"total_sales_units_sf\":null,\"regular_sales_dollars_af\":null,\"sub_category\":[\"REFRIGERATOR\"],\"base_sales_dollars_af\":null,\"total_sales_dollars_af\":null,\"promotional_sales_units_af\":null,\"base_sales_dollars_uf\":null,\"base_sales_margin_dollars_uf\":null,\"created_by_id\":\"2\",\"base_cost_dollars_min_uf\":null,\"total_sales_dollars_af_rel_diff_to_td\":null,\"primary_image_id\":null,\"base_sales_margin_dollars_td\":null,\"adjusted_cost_dollars\":null,\"base_aur_uf\":null,\"sales_units_lift_td\":null,\"regular_sales_margin_dollars_if\":0,\"promo_description\":null,\"size_description\":null,\"lift_vs_spend_percentage_uf\":null,\"sales_units_lift_percentage_if\":0,\"sales_dollars_lift_uf\":null,\"base_sales_units_sf\":null,\"vehicle\":\"EDLP\",\"base_sales_margin_dollars_af\":null,\"cost_dollars\":null,\"sub_category_id\":[\"9112\"],\"average_unit_retail\":null,\"total_sales_units_if\":null,\"regular_price_dollars_max_uf\":null,\"cloned_from\":null,\"max_price\":null,\"promo_start_date\":\"2024-04-04\",\"sales_dollars_lift_percentage_af\":null,\"regular_sales_units_td\":null,\"max_cost\":null,\"promotional_sales_margin_dollars_td\":null,\"prevailing_price\":null,\"promo_type\":\"Amount Off\",\"base_aur_if\":null,\"assignee_name\":\"Promo Planner\",\"regular_sales_margin_dollars_uf\":0,\"batch_processed\":false,\"promo_auc_sf\":null,\"base_auc_af\":null,\"min_cost\":null,\"sales_dollars_lift_percentage_if\":null,\"total_sales_margin_dollars_uf\":null,\"sales_margin_dollars_lift_percentage_sf\":null,\"regular_sales_units_if\":0,\"total_sales_dollars_sf_rel_diff_to_td\":null,\"total_sales_units_td_rel_diff_to_af\":null,\"total_sales_units_uf\":null,\"value_to_customer_percentage\":null,\"base_sales_units_af\":null,\"total_sales_margin_dollars_if\":null,\"sales_dollars_lift_if\":null,\"base_sales_dollars_td\":null,\"commentator_user_id\":\"2\",\"promo_group_id\":null,\"total_sales_margin_percentage_if\":null,\"promo_aur_sf\":null,\"promotional_sales_margin_dollars_sf\":null,\"promotional_margin_percentage\":null,\"department_id\":[\"14\"],\"computing\":false,\"total_sales_dollars_td\":null,\"large_promo\":false,\"base_aur_sf\":null,\"regular_sales_margin_dollars_af\":null,\"versions_ids\":[],\"nullify_overridable\":null,\"total_sales_units_af_rel_diff_to_td\":null,\"total_sales_margin_percentage_uf\":null,\"regular_sales_margin_dollars_sf\":null,\"created_from_event_id\":null,\"lift_vs_spend_percentage_td\":null,\"lift_vs_spend_percentage_af\":null,\"drift_changed\":null,\"sales_units_lift_percentage_td\":null,\"product_count\":23,\"brand\":[\"FRIGIDAIRE\",\"WHIRLPOOL\",\"SAMSUNG\",\"LG\",\"BOSCH\"],\"base_auc_sf\":null,\"created_from_invitation_id\":null,\"sales_units_lift_sf\":null,\"promo_status\":\"Ready\",\"sales_margin_dollars_lift_af\":null,\"advertising_instructions\":\"\",\"promotional_sales_units_td\":null,\"redemption_count_td\":null,\"base_sales_margin_dollars_if\":null,\"sub_department_id\":[],\"approval_date\":null,\"submission_status\":null,\"promotional_sales_dollars_af\":null,\"total_sales_units_td_rel_diff_to_sf\":null,\"total_sales_units_af\":null,\"promotional_sales_margin_dollars_uf\":null,\"base_auc_uf\":null,\"department\":[\"APPLIANCES\"],\"sales_dollars_lift_af\":null,\"total_sales_units_td\":null,\"sales_units_lift_percentage_uf\":0,\"secondary_image_id\":null,\"promo_name\":\"alooooo\",\"promo_auc_uf_override\":null,\"regular_sales_units_uf\":0,\"fiscal_week\":[\"2024-03-31\"],\"sub_department\":[],\"sales_dollars_lift_sf\":null,\"regular_sales_dollars_sf\":null,\"promotional_sales_units_uf\":null,\"fiscal_quarter\":[\"202403\"],\"lift_vs_spend_percentage_if\":null,\"promotional_sales_units_sf\":null,\"sales_dollars_lift_percentage_uf\":null,\"regular_sales_dollars_td\":null,\"units_per_redemption_td\":null,\"base_cost_dollars_max_uf\":null,\"promo_aur_af\":null,\"base_sales_margin_dollars_sf\":null,\"has_comments\":false,\"total_sales_margin_percentage_af\":null,\"discount_percentage\":null,\"promo_aur_if\":null,\"internal_contribution\":null,\"promotional_sales_discount_dollars_if\":null,\"promo_id\":\"P00000011\",\"used_in\":null,\"forecast_gross_margin_dollars\":null,\"seeded_deal_number\":null,\"total_sales_margin_dollars_sf_rel_diff_td\":null,\"sales_units_lift_uf\":0,\"sales_margin_dollars_lift_sf\":null,\"total_sales_margin_dollars_td\":null,\"product_group_ids\":[\"86b224c3-1bd6-44fd-87e8-99c016018ed3\"],\"total_sales_dollars_sf\":null,\"sales_units_lift_if\":0,\"primary_objective\":null,\"promotional_margin_dollars\":null,\"sales_margin_dollars_lift_percentage_uf\":null,\"total_sales_dollars_if\":null,\"total_sales_dollars_uf\":null,\"overridden_metrics\":[],\"sales_units_lift_af\":null,\"content\":\"\",\"sales_dollars_lift_td\":null,\"total_sales_units_sf_rel_diff_to_td\":null,\"media\":null,\"sales_margin_dollars_lift_percentage_af\":null,\"promo_auc_uf\":null,\"promotional_sales_dollars_uf\":null,\"sales_margin_dollars_lift_percentage_if\":null,\"category\":[\"MAJOR KITCHEN APPLIANCE\"],\"location_count\":125,\"bold_ad_text\":\"\",\"promo_auc_af\":null,\"total_sales_margin_percentage_td\":null,\"average_unit_cost\":null,\"sales_dollars_lift_percentage_td\":null,\"category_id\":[\"432\"],\"created_at\":\"1712132339\",\"submitted_by\":null,\"promo_auc_if\":null,\"discount_dollars\":20,\"advertising_bug\":null,\"sales_dollars_lift_percentage_sf\":null,\"total_sales_margin_dollars_sf\":null,\"auc_changed\":null,\"regular_sales_units_af\":null,\"promotional_sales_dollars_td\":null,\"regular_price_dollars\":null,\"base_sales_dollars_sf\":null,\"regular_sales_dollars_uf\":0,\"promotional_sales_units_if\":null,\"is_reopened\":\"Yes\",\"sales_margin_dollars_lift_if\":null,\"vendor_funding\":null,\"forecast_sales_dollars\":null,\"finalized_event_ids\":[],\"regular_sales_units_sf\":null,\"total_sales_margin_dollars_af\":null,\"regular_price_dollars_min_uf\":null,\"promo_expression\":\"GET $20.00 off firas\",\"lift_vs_spend_percentage_sf\":null,\"promotional_sales_margin_dollars_af\":null,\"updated_at\":\"1712143155\",\"category_manager\":null,\"regular_sales_dollars_if\":0,\"rejected_by\":null,\"value_to_customer_dollars\":null,\"base_sales_dollars_if\":null,\"promotional_price_dollars\":null,\"promo_end_date\":\"2024-04-04\",\"approved_by\":null,\"redemption_rate_td\":null,\"sales_margin_dollars_lift_percentage_td\":null,\"total_sales_margin_dollars_af_rel_diff_to_td\":null,\"base_sales_units_uf\":null,\"internal_comments\":\"\",\"vendor_promo_expense_percentage\":null,\"aur_changed\":null,\"internal_promo_expense_percentage\":null,\"promo_aur_uf\":null,\"parity_id\":null,\"promo_group_name\":null,\"card_type\":\"No Card\",\"regular_sales_margin_dollars_td\":null,\"min_price\":null,\"total_sales_margin_percentage_sf\":null,\"promotional_sales_dollars_if\":null,\"forecast_sales_units\":null,\"sales_units_lift_percentage_af\":null,\"commentator_user_name\":\"Promo Planner\"}","kubernetes":{"pod_name":"promo-planning-service-76776b9dc4-rcwmx","namespace_name":"frigui-dev","pod_id":"9b19b1e5-cf06-4b7a-acd5-5ea5f4d110d3","labels":{"app":"promo-planning-service","pod-template-hash":"76776b9dc4"},"annotations":{"linkerd.io/inject":"enabled","rollme":"UlGwE"},"host":"aks-mainpool-16235376-vmss00000u","container_name":"promo-planning-service","docker_id":"8f617240848c3e36bbc0eded06d6528f73b1973571ca44c0f72e57b9516963ab","container_hash":"devcognira.azurecr.io/cognira/promoai/promo-planning-service@sha256:5fe7d5220c01caee441e756cd7314e0fcb5d47864f1eeae170c2d7734718c96d","container_image":"devcognira.azurecr.io/cognira/promoai/promo-planning-service:frigui-logs"}}""" // Output: {"name":"John","age":30}
}