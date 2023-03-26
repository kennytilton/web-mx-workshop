(ns ^:figwheel-hooks shop.quickstart.core
  (:require
    [goog.dom :as gdom]
    [bide.core :as r]
    [tiltontec.matrix.api
     :refer [with-mx-trace with-minfo with-minfo-std mx-type md-state
             matrix mpar mget mget? mset! mswap! mset! fasc fmu minfo
             cinfo cF cF+ cFn cFonce cI cf-freeze] :as mxapi]
    [tiltontec.web-mx.api
     :refer [evt-md target-value tag-dom-create style-string
             title img section h1 h2 h3 input footer p a b h4 u table th tr td
             blockquote span i label ul li div button br pre code]]

    [shop.quickstart.core.style :as style]
    [shop.quickstart.core.widget :as w]
    [shop.quickstart.lesson :as lesson]))

;;; --- user inputs -------------------------------------

(defn- qs-router-starter [lessons]
  (fn []
    (r/start! (r/router
                (into [] (concat [["/" :intro]]
                           (map (fn [{:keys [route]}]
                                  [(str "/" (name route)) route])
                             lessons))))
      {:default     :ignore
       :on-navigate (fn [route params query]
                      (when-let [mtx @matrix]
                        (mset! mtx :route route)))})))

(defn- qs-keydown-handler []
  (cF+ [:watch (fn [property-name me new-value old-value cell]
                 ;; If we thought we would be changing the handler during
                 ;; the life of the app, we would have to worry about
                 ;; removing the 'old-value' listener.
                 (.addEventListener js/document "keydown" new-value))]
    ;; The astute reader may notice that the fn is decided without
    ;; reactive consultation with other state. So why is this a formula?
    ;; In this case, we just want to simulate an object constructor and
    ;; have this code run at app init time, but with the app instance
    ;; in view so we can close over the anaphor 'me'.
    ;;
    ;; This pseudo-constructor usage is commonplace in W/mx apps.
    ;;
    (fn [evt]
      ;; This formula cell expects to be bound to a property of
      ;; an object ("me") where lesson data is kept, so there
      ;; is no need to navigate to the desired data -- just `(mget me ...)`.
      (let [lessons (mget me :lessons)
            curr-x (.indexOf lessons
                     (mget me :selected-lesson))]
        (when-let [new-x (case (.-key evt)
                           "Home" 0
                           "End" (dec (count lessons))
                           ("ArrowRight" "ArrowDown" "PageDown") (inc curr-x)
                           ("ArrowLeft" "ArrowUp" "PageUp") (dec curr-x)
                           nil)]
          (when (<= 0 new-x (dec (count lessons)))
            (.stopPropagation evt)
            (.preventDefault evt)
            ;; Likewise, "me" is expected to be the app proxy object
            ;; where routing changes land:
            (mset! me :route (:route (nth lessons new-x)))))))))

;;; --- the quick-start app beef --------------------------------

(defn quick-start [lesson-title lessons]
  (div {:style (style/qs-app)}
    {:name            :quick-start
     ;; next, we wrap the current route in an "input" (cI) cell
     ;; because the user will be an "outside" source for its values.
     :route           (cI :intro)
     :router-starter  (qs-router-starter lessons)
     :selected-lesson (cF (some #(when (= (mget me :route) (:route %)) %)
                            lessons))
     :keydowner       (qs-keydown-handler)
     :lessons         lessons
     :show-glossary?  (cI false)}

    (div {:style (style/qs-dash)}
      (span {:style {:font-size  "24px"
                     :padding    "18px"
                     :text-align :center}}
        lesson-title)
      (span "use <- or -> keys<br>&nbsp;")
      (w/quick-start-toolbar))

    (div
      (when-let [lesson (mget (fasc :quick-start me :me? true :dbg :sanity) :selected-lesson)]
        (w/lesson-display lesson)))))

(defn main [mx-builder]
  (println "[main]: loading app")

  (let [root (gdom/getElement "app")
        ;; ^^^ "app" must be ID of DIV defined in index.html
        app-matrix (mx-builder)
        app-dom (tag-dom-create app-matrix)]
    (reset! matrix app-matrix)
    (set! (.-innerHTML root) nil)
    (gdom/appendChild root app-dom)

    (when-let [router-starter (mget? app-matrix :router-starter)]
      (router-starter))))

(main #(quick-start "Web/MX&trade;<br>Quick Start"
         (lesson/qs-lessons)))

;
;(defn ^:after-load on-reload []
;  ;; optionally touch your app-state to force rerendering depending on
;  ;; your application
;  ;; (swap! app-state update-in [:__figwheel_counter] inc)
;)
