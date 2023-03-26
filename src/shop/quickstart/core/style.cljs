(ns shop.quickstart.core.style)
;;;
;;; Note that bringing CSS in-line does not mean we cannot collect it all
;;; in one place for convenient editing together.
;;;
(defn qs-app []
  {:height         "100vh" :margin 0 :padding 0
   :display        :flex
   :flex-direction :horizontal})

(defn qs-dash []
  {:display         :flex
   :flex-direction  :column
   :height          "100%"
   :margin          0
   :padding         0
   :min-width       "180px"
   :align-items     :center
   :justify-content :start
   :gap             "1em"
   :border-right    "4mm ridge orange"})

(defn lesson-live []
  {:border-color "orange",
   :border-style "solid",
   :border-width "2px",
   :max-width "40em",
   :min-width "40em",
   :margin-top "9px"})

(defn lesson-style []
  {:display        :flex
   :overflow-y     :auto
   :flex-direction :column
   :padding        "4em"
   :height         "100%"})

(defn lesson-anchor []
  {:min-width       "128px"
   :text-decoration :none
   :border-width    "2px"
   :border-style    :solid
   :border-color    "white"
   :font-weight     "normal"})

(defn lesson-toolbar []
  {:margin          0
   :overflow-y      :scroll
   :gap             "9px"
   :display         :flex
   :flex-direction  :column
   :align-items     :start
   :justify-content :start})

(defn lesson-top []
  {:display "flex",
   :flex-direction "column",
   :align-items "center",
   :justify-content "start",
   :gap "1em",
   :padding "9px"})

(defn push-button []
  {:min-width "1em",
   :color "#333333",
   :text-align "center",
   :font-size "64px",
   :background-color "rgba(51, 51, 51, 0.05)",
   :cursor "pointer",
   :border-width "0",
   :border-radius "16px",
   :font-family
   "\"Haas Grot Text R Web\", \"Helvetica Neue\", Helvetica, Arial, sans-serif"})

(defn pushbutton []
  {:line-height "20px",
   :color "#333333",
   :text-align "center",
   :vertical-align "baseline",
   :white-space "nowrap",
   :font-size "14px",
   :transition "all 200ms",
   :font-weight "500",
   :list-style "none",
   :-webkit-user-select "none",
   :background-color "rgba(51, 51, 51, 0.05)",
   :cursor "pointer",
   :border-width "1px",
   :padding "10px 12px",
   :display "inline-block",
   :border-radius "8px",
   :touch-action "manipulation",
   :user-select "none",
   :font-family
   "\"Haas Grot Text R Web\", \"Helvetica Neue\", Helvetica, Arial, sans-serif"})

(defn digi-readout []
  {:font-size "96px",
   :font-family
   "digital-clock-font, HelveticaNeue-UltraLight, Helvetica",
   :padding "16px 24px",
   :min-width "325px",
   :color "cyan",
   :text-align "center",
   :background "black",
   :user-select "none"})

(defn passage []
  {:font-family "Roboto, sans-serif",
   :font-size "18px",
   :max-width "35em",
   :margin-bottom "6px"})

(defn hilite-quote []
  {:background "#f9f9f9",
   :border-left "5px solid #ff5f1f",
   :font-size "20px",
   :margin "0 1em 2em 1em",
   :padding "1em 20px",
   :quotes "\"\\201C\"\"\\201D\"\"\\2018\"\"\\2019\""})

(defn heavy-char []
  {:stroke "black",
   :fill "black",
   :font "bold 48px sans-serif",
   :user-select "none",
   :-webkit-user-select "none",
   :touch-action "manipulation"})