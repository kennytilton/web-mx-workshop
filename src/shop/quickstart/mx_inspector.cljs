(ns shop.quickstart.mx-inspector
  (:require
    [goog.dom :as gdom]
    [goog.object :as gobj]
    [cljs.pprint :as pp]
    [tiltontec.cell.poly :refer [md-quiesce] :as mxpoly]
    [tiltontec.matrix.api
     :refer [with-mx-trace with-minfo with-minfo-std mx-type md-state md-ref?
             matrix mpar mget mget? mset! mswap! mset! fasc fmu minfo
             cinfo cF cF+ cFn cFonce cI cf-freeze] :as mx]
    [tiltontec.web-mx.api
     :refer [evt-md target-value tag-dom-create style-string jso-select-keys dom-tag
             title img section h1 h2 h3 input footer p a b h4 u table th tr td
             blockquote span i label ul li div button br pre code] :as wmx]))

;;; --- individual property view ---------------------------------------

(defn mxi-md-prop-view [md prop-key]
  (div {:style {:display        :flex
                :flex-direction :row
                :align-items    :center
                :padding        "6px"}}
    ; prop name as label:
    (span {:style (assoc {:min-width "8em"}
                    :background (when-let [pv (prop-key (:cz (meta md)))]
                                  (case (mx-type pv)
                                    :tiltontec.cell.base/c-formula :aqua
                                    :tiltontec.cell.base/cell :lime
                                    nil)))
           :title (when-let [pv (prop-key (:cz (meta md)))]
                    ;; we cannot show code for cI input cells because
                    ;; cI is a function and thus evaluates the value parameter
                    (case (mx-type pv)
                      :tiltontec.cell.base/c-formula
                      (with-out-str
                        (binding [*print-level* false]
                          (pp/pprint (first (:code @pv)))))
                      nil))}
      prop-key)
    ; prop value:
    (span {:style {:background :white
                   :padding    "0.5em"}
           :title (str (prop-key @md))}
      (subs (str (prop-key @md)) 0 40))))

;;; --- the recursive model inspector ---------------------------------

(defn mxi-md-view-on-click [md]
  ;; the cell-generating macros all expand to 'make-cell' or 'make-c-formula',
  ;; so we can just move them into top-level functions to break up the code.
  (cF (fn [e]
        (.stopPropagation e)
        (let [ex (wmx/jso-select-keys e
                   [:type :metaKey :altKey :shiftKey :ctrlKey])]
          (cond
            (= ex {:type   "click" :metaKey false
                   :altKey true :shiftKey false :ctrlKey false})
            (mswap! me :show-props? not)

            (= ex {:type   "click" :metaKey false
                   :altKey false :shiftKey false :ctrlKey false})
            (mswap! me :show-kids? not)

            :else (prn :ignoring ex))))))

(declare mxi-md-view)

(defn mxi-md-view-rich [md]
  (div
    {:onclick (mxi-md-view-on-click md)}
    {:name        :md-view
     :md          md
     :show-props? (cI false)
     :show-kids?  (cI false)}
    ;; md label
    (span {:style {:user-select :none}}
      (str (mget? md :tag) ":" (mget? md :name)
        (when-let [ks (mget? md :kids)]
          (str ":" (count ks)))))
    ;; ...md props
    (div {:style {:padding-left "2em"}} {}
      (when (mget (fasc :md-view me) :show-props?)
        (mapv #(mxi-md-prop-view md %)
          (remove #{:tag :id :attr-keys :parent :name :kids}
            (keys @md)))))
    ;; ...md kids
    (div {:style {:padding-left "1em"}} {}
      (when (mget (fasc :md-view me) :show-kids?)
        (mapv #(mxi-md-view %) (mget? md :kids))))))

(defn mxi-md-view [md]
  (cond
    (string? md) (span md)
    (md-ref? md) (mxi-md-view-rich md)
    :else (span (str "?:" md))))

(defn inspector-toolbar []
  (div {:style {:display         :flex
                :justify-content :space-between
                :background      :cornsilk
                :padding         "3px"
                :gap             "1em"}}
    (span "inspector toolbar")                              ; placeholder
    (button {:cursor  :pointer
             :onclick (fn [evt]
                        ; todo break this out as inspector-uninstall then make keychord a toggle
                        (let [mxi (fasc :mxi (evt-md evt))
                              dom (gdom/getElement "inspector")]
                          (md-quiesce mxi)
                          (set! (.-innerHTML dom) nil)))}
      ;; todo find a nice "close" icon?
      "X")))

(defn inspector-mx [mx]
  (div {:style {:background :linen}}
    {:name   :mxi
     :target mx}
    (inspector-toolbar)
    (ul
      (li (i "click to show/hide children"))
      (li (i "option click to show/hide properties")))
    (div {:style {:padding "0 12px 12px 12px"}}
      (mxi-md-view mx))))

(defn inspector-install []
  (let [ins (gdom/getElement "inspector")]
    (set! (.-innerHTML ins) nil)
    (gdom/appendChild ins
      (tag-dom-create
        ;; we continue cheating and grab the target MX from a global atom.
        ;; Eventually we will want sth less brittle here, so the `matrix` global
        ;; need not be populated.
        (inspector-mx @matrix)))))

