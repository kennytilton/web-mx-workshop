(ns shop.quickstart.mx-inspector
  (:require
    [goog.dom :as gdom]
    [tiltontec.cell.poly :refer [md-quiesce] :as mxpoly]
    [tiltontec.matrix.api
     :refer [with-mx-trace with-minfo with-minfo-std mx-type md-state md-ref?
             matrix mpar mget mget? mset! mswap! mset! fasc fmu minfo
             cinfo cF cF+ cFn cFonce cI cf-freeze] :as mx]
    [tiltontec.web-mx.api
     :refer [evt-md target-value tag-dom-create style-string jso-select-keys dom-tag
             title img section h1 h2 h3 input footer p a b h4 u table th tr td
             blockquote span i label ul li div button br pre code] :as wmx]))

;;; step #3: primitive, recursive MX node display



(defn mxi-md-view [md]
  (cond
    (string? md) (span md)
    (md-ref? md) (do
                   (div
                     {:onclick
                      ;; the easiest way to connect an event back to this DIV
                      ;; is to spawn the callback in a formula, and close over "me":
                      #_(cF (fn [e]
                              (let [ck (evt-md e)]
                                (.stopPropagation e)
                                (mswap! me :expanded? not))))
                      ;; a second way is to ask w/mx which model is connected to
                      ;; the event, then navigate up to the name :md-view, inclusively
                      ;; in case :md-view itself was clicked.
                      #(let [mdv (fasc :md-view (evt-md %) :me? true)]
                         (.stopPropagation %)
                         (mswap! mdv :expanded? not))

                      :style (cF (when (mget me :expanded?)
                                   {:color :red}))}
                     {:name      :md-view
                      :md        md
                      :expanded? (cI false)}
                     (span {:style {:user-select :none}}
                       (mget? md :name
                             (mget? md :tag "noname")))
                     ;; ^^^ no tag should not occur, wmx builds that in
                     (div {:style {:padding-left "1em"}} {}
                       (when (mget (fasc :md-view me) :expanded?)
                         (mapv #(mxi-md-view %) (mget? md :kids))))))
    :else (span "not string or md")))

;;; step #4: complete lifecycle with "close" control
(defn inspector-toolbar []
  (div {:style {:display         :flex
                :justify-content :space-between
                :margin          "2em"
                :gap             "1em"}}
    (span "inspector toolbar")
    (button {:cursor  :pointer
             :onclick (fn [evt]
                        ; todo break this out as inspector-uninstall then make keychord a toggle
                        (let [mxi (fasc :mxi (evt-md evt))
                              dom (gdom/getElement "inspector")]
                          (md-quiesce mxi)
                          (set! (.-innerHTML dom) nil)))}
      ;; todo find a nice "close" icon?
      "X")))

;;; step #3: build an inspector
(defn inspector-mx [mx]
  (div {:style {:background :linen
                :padding "1em"}}
    {:name   :mxi
     :target mx}
    (inspector-toolbar)
    (mxi-md-view mx)))

;;; step #1: see shop.quickstart.core:47 for option-cmd-X keychord recognition
;;; step #2: explicit DOM manipulation to get inspector installed
;;; normally wmx handles dynamic dom, but we want the inspector to
;;; jump in/out without the app's involvement.
(defn inspector-install []
  (let [app (gdom/getElement "inspector")]
    (set! (.-innerHTML app) nil)
    (gdom/appendChild app
      (tag-dom-create
        ;; we continue cheating and grab the target MX from a global atom.
        ;; Eventually we will want sth less brittle here, so the `matrix` global
        ;; need not be populated.
        (inspector-mx @matrix)))))

