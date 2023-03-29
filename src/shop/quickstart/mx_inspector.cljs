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
(defn mxi-md-view [md depth]
  (when (pos? depth)
    (cond
      (string? md) (span md)
      (md-ref? md) (do
                     (div {}
                       {:md md}
                       (span (mget? md :name
                               (mget? md :tag "noname")))
                       ;; ^^^ no tag should not occur, wmx builds that in
                       (div {:style {:padding-left "1em"}} {}
                         (mapv #(mxi-md-view % (dec depth)) (mget? md :kids)))))
      :else (span "not string or md"))))

;;; step #4: complete lifecycle with "close" control
(defn inspector-toolbar []
  (div {:style {:justify-content :space-between
                :margin "2em"
                :gap "1em"
                :display :flex}}
    (span "inspector toolbar")
    (span {:onclick (fn [evt]
                      ; todo break this out as inspector-uninstall then make keychord a toggle
                      (let [mxi (fasc :mxi (evt-md evt))
                            dom (gdom/getElement "inspector")]
                        (md-quiesce mxi)
                        (set! (.-innerHTML dom) nil)))}
      ;; todo find a nice "close" icon?
      "[X]")))

;;; step #3: build an inspector
(defn inspector-mx [mx]
  (div {}
    {:name   :mxi
     :target mx}
    (inspector-toolbar)
    (mxi-md-view mx 3)))

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

