(ns shop.quickstart.core.widget
  (:require
    [tiltontec.matrix.api
     :refer [with-mx-trace with-minfo with-minfo-std mx-type md-state
             matrix mpar mget mget? mset! mswap! mset! fasc fmu minfo
             cinfo cF cF+ cFn cFonce cI cf-freeze] :as mxapi]
    [tiltontec.web-mx.api
     :refer [evt-md target-value tag-dom-create style-string
             title img section h1 h2 h3 input footer p a b h4 u table th tr td
             blockquote span i label ul li div button br pre code]]
    [shop.quickstart.core.style :as style]
    [shop.quickstart.core.glossary :as glossary]))

(defn text-block [text]
  (when text
    (if (string? text)
      (blockquote {:style (merge
                            (style/hilite-quote)
                            (style/passage))} text)
      (doall (for [elt text]
               (p {:style (style/passage)} elt))))))

(defn quick-start-toolbar []
  (div {:style (style/lesson-toolbar)}
    (doall
      ;; Web/MX uses a dynamic *parent* binding to automate parent-child linkage,
      ;; so we use 'doall' to force children construction before the binding scope is exited.
      (for [{:keys [menu title route] :as lesson} (mget (fasc :quick-start me) :lessons)]
        (a {:href     (str "#/" (name route))
            :selector menu
            :style    (cF (let [curr-route (mget (fasc :quick-start me) :route)]
                            ;; if we do not like coding (fasc :quick-start me) twice,
                            ;; we can convert it to a `letfn [(qs []...)]`, or resolve
                            ;; it in a LET outside the FOR loop.
                            (merge
                              (style/pushbutton)
                              (style/lesson-anchor)
                              (when (= route curr-route)
                                {:border-color "orange"
                                 :font-weight  "bold"}))))}
          {:name (str "anchor-" route)}
          (or menu title))))))

(defn lesson-display [lesson]
  (div {:class :fade-in                             ;
        :style (style/lesson-style)}
    (h2 (:title lesson))
    (text-block
      (:preamble lesson))
    (div {:style (style/lesson-live)}
      ((:builder lesson)))
    (pre {:style {:background "LightYellow"
                  :padding "12px"
                  :margin "12px 48px 0 0"}}
      (code {:style {:font-size "14px"}}
        (:code lesson)))
    (glossary/glossary)
    (text-block
      (:comment lesson))))